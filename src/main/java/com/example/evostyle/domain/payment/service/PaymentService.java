package com.example.evostyle.domain.payment.service;

import com.example.evostyle.domain.brand.entity.Brand;
import com.example.evostyle.domain.brand.repository.BrandRepository;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentCheckoutResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.dto.response.TossPaymentResponse;
import com.example.evostyle.domain.payment.entity.Payment;
import com.example.evostyle.domain.payment.repository.PaymentRepository;
import com.example.evostyle.global.exception.ConflictException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.InvalidException;
import com.example.evostyle.global.exception.NotFoundException;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.Base64;
import java.util.List;

@Service
@Slf4j
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class PaymentService {

    private final PaymentRepository paymentRepository;
    private final MemberRepository memberRepository;
    private final OrderRepository orderRepository;

    @PersistenceContext
    private EntityManager em;

    @Transactional
    public PaymentResponse completeConfirmFlow(TossPaymentResponse tossResponse, Long orderId) {

        Order order = orderRepository.findById(orderId)
                        .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        order.getOrderItemList().forEach(orderItem -> {orderItem.updateOrderStatus(OrderStatus.PAID);});

        for (OrderItem orderItem : order.getOrderItemList()) {
            orderItem.getProductDetail().deductStock(orderItem.getEachAmount());
        }

        Member member = order.getMember();
        member.addToPurchaseSum(order.getTotalAmountSum());
        member.promoteGrade();

        Payment payment = Payment.of(order, tossResponse);

        paymentRepository.save(payment);
        return PaymentResponse.from(payment);
    }

    @Transactional
    public void completeCancelFlow(String paymentKey){
        //payment 키로 결제를 찾아서 오더 상품을 찾은다음에 그게 가지고 있는 상품디테일의 재고를 돌려놓는다
        Payment payment = paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PAYMENT));

        Member member = payment.getOrder().getMember();
        member.minusToPurchaseSum(payment.getTotalAmount());
        payment.getOrder().getOrderItemList().forEach(o -> o.updateOrderStatus(OrderStatus.CANCELED));
    }



    public List<PaymentResponse> findByMember(Long memberId) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return paymentRepository.findByMemberId(memberId)
                .stream().map(PaymentResponse::from).toList();
    }

    public PaymentResponse findByPaymentKey(String paymentKey) {
        Payment payment =  paymentRepository.findByPaymentKey(paymentKey)
                .orElseThrow(() -> new NotFoundException(ErrorCode.NOT_FOUND_PAYMENT));

        return PaymentResponse.from(payment);
    }
}
