package com.example.evostyle.domain.payment.service;

import com.example.evostyle.common.util.JsonHelper;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.entity.OrderStatus;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.payment.dto.event.PaymentCanceledEvent;
import com.example.evostyle.domain.payment.dto.event.PaymentConfirmEvent;
import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
@RequiredArgsConstructor
public class PaymentKafkaListener {

    private final JsonHelper jsonHelper;
    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;

    //주문완료
    @Transactional
    @KafkaListener(topics = "payment-completed", groupId = "stock-deduction-group")
    public void decreaseStock(String payload){
        PaymentConfirmEvent paymentConfirmEvent = jsonHelper.fromJson(payload, PaymentConfirmEvent.class);

        Order order = orderRepository.findById(paymentConfirmEvent.orderId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        order.getOrderItemList().forEach(i -> {
            ProductDetail productDetail = i.getProductDetail();
            productDetail.deductStock(i.getEachAmount());
        });
    }

    @Transactional
    @KafkaListener(topics = "payment-completed", groupId = "order-status-group")
    public void updateOrderStatusUpdate(String payload){

        PaymentConfirmEvent paymentConfirmEvent = jsonHelper.fromJson(payload, PaymentConfirmEvent.class);

        Order order = orderRepository.findOrderWithDetails(paymentConfirmEvent.orderId());

        order.getOrderItemList().forEach(i -> i.updateOrderStatus(OrderStatus.PAID));

    }

    @Transactional
    @KafkaListener(topics = "payment-completed", groupId = "member-grade-group")
    public void handleMemberGrade(String payload){
        PaymentConfirmEvent paymentConfirmEvent = jsonHelper.fromJson(payload, PaymentConfirmEvent.class);

        Order order = orderRepository.findById(paymentConfirmEvent.orderId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        Member member = memberRepository.findById(paymentConfirmEvent.memberId())
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.addToPurchaseSum(order.getTotalPriceSum());
        member.promoteGrade();
    }


    //결제 취소 완료
    @Transactional
    @KafkaListener(topics = "payment-canceled", groupId = "stock-restore-group")
    public void restoreStockAfterCancel(String payload){
        //재고 차감 내용 작성하기

        PaymentCanceledEvent canceledEvent = jsonHelper.fromJson(payload, PaymentCanceledEvent.class);

        Order order = orderRepository.findOrderWithDetails(canceledEvent.orderId());

        for(OrderItem orderItem : order.getOrderItemList()){
            orderItem.getProductDetail().restoreInventory(orderItem.getEachAmount());
        }
    }

    @Transactional
    @KafkaListener(topics = "payment-canceled", groupId = "member-restore-group")
    public void rollbackMemberGradeAndAmount(String payload){

        PaymentCanceledEvent canceledEvent = jsonHelper.fromJson(payload, PaymentCanceledEvent.class);
        Order order = orderRepository.findOrderWithDetails(canceledEvent.orderId());
        Member member = order.getMember();

        member.minusToPurchaseSum(order.getTotalPriceSum());
        member.promoteGrade();
    }

    @Transactional
    @KafkaListener(topics = "payment-canceled", groupId = "order-cancel-group")
    public void changeStatusToCanceled(String payload){
        PaymentCanceledEvent canceledEvent = jsonHelper.fromJson(payload, PaymentCanceledEvent.class);
        Order order = orderRepository.findOrderWithDetails(canceledEvent.orderId());

        order.getOrderItemList().forEach(i -> i.updateOrderStatus(OrderStatus.CANCELED));
    }
}
