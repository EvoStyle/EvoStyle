package com.example.evostyle.domain.order.service;

import com.example.evostyle.domain.member.entity.Authority;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.dto.request.CreateOrderItemRequest;
import com.example.evostyle.domain.order.dto.response.ReadOrderItemResponse;
import com.example.evostyle.domain.order.dto.response.ReadOrderResponse;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.entity.OrderItem;
import com.example.evostyle.domain.order.repository.OrderItemQueryDsl;
import com.example.evostyle.domain.order.repository.OrderQueryDsl;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.product.entity.Product;
import com.example.evostyle.domain.product.entity.ProductDetail;
import com.example.evostyle.domain.product.productdetail.entity.ProductDetail;
import com.example.evostyle.domain.product.repository.ProductDetailRepository;
import com.example.evostyle.global.exception.BadRequestException;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.NotFoundException;
import com.mysema.commons.lang.Pair;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class OrderService {

    private final OrderRepository orderRepository;
    private final MemberRepository memberRepository;
    private final ProductDetailRepository productDetailRepository;
    private final OrderItemQueryDsl orderItemQueryDsl;
    private final OrderQueryDsl orderQueryDsl;

    @Transactional
    public Pair<Order, Map<Long, ProductDetail>> createOrder(Long memberId, List<CreateOrderItemRequest> requestList) {
        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        List<Long> productDetailIdList = requestList.stream()
                .map(CreateOrderItemRequest::productDetailId)
                .toList();

        List<ProductDetail> productDetailList = productDetailRepository.findAllById(productDetailIdList);

        if (productDetailList.size() != productDetailIdList.size()) {
            throw new NotFoundException(ErrorCode.PRODUCT_DETAIL_NOT_FOUND);
        }

        Map<Long, ProductDetail> idToProductDetail = productDetailList.stream()
                .collect(Collectors.toMap(ProductDetail::getId, productDetail -> productDetail));

        int totalAmountSum = 0;
        int totalPriceSum = 0;

        for (CreateOrderItemRequest request : requestList) {
            totalAmountSum += request.eachAmount();
            totalPriceSum += idToProductDetail.get(request.productDetailId()).getProduct().getPrice() * request.eachAmount();
        }

        Order order = Order.of(member, totalAmountSum, totalPriceSum);

        orderRepository.save(order);

        return Pair.of(order, idToProductDetail);
    }

    public List<ReadOrderResponse> readAllOrders(Long memberId) {

        Member member = memberRepository.findById(memberId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (member.getAuthority() != Authority.OWNER) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }
        List<OrderItem> orderItemList = orderItemQueryDsl.findByOwnerId(memberId);

        Map<Long, List<OrderItem>> idToOrderItemList = orderItemList.stream()
                .collect(Collectors.groupingBy(orderItem -> orderItem.getOrder().getId()));

        List<Long> orderIdList = new ArrayList<>(idToOrderItemList.keySet());

        return orderIdList.stream()
                .map(orderId -> {
                    List<ReadOrderItemResponse> readOrderItemResponseList = idToOrderItemList.get(orderId)
                            .stream()
                            .map(ReadOrderItemResponse::from)
                            .toList();

                    int totalAmountSum = readOrderItemResponseList.stream()
                            .mapToInt(ReadOrderItemResponse::eachAmount)
                            .sum();

                    int totalPriceSum = readOrderItemResponseList.stream()
                            .mapToInt(ReadOrderItemResponse::totalPrice)
                            .sum();

                    return ReadOrderResponse.from(
                            orderId,
                            readOrderItemResponseList,
                            totalAmountSum,
                            totalPriceSum
                    );
                }).toList();
    }

    @Transactional
    public void deleteOrder(Long orderId, Long memberId) {
        Order order = orderQueryDsl.findByIdWithItems(orderId)
                .orElseThrow(() -> new NotFoundException(ErrorCode.ORDER_NOT_FOUND));

        validateOrderOwnedByMember(memberId, order);

        orderRepository.delete(order);
    }

    @Transactional
    public void updateOrder(OrderItem orderItem, Long memberId, int newAmount) {
        Order order = orderItem.getOrder();

        validateOrderOwnedByMember(memberId, order);

        int newTotalAmountSum = order.getTotalAmountSum() - orderItem.getEachAmount() + newAmount;
        int newTotalPriceSum = order.getTotalPriceSum() - orderItem.getTotalPrice() + (orderItem.getProductPrice() * newAmount);

        order.updateAmountSumAndPriceSum(newTotalAmountSum, newTotalPriceSum);
    }

    @Transactional
    public void adjustOrderAfterItemCancellation(OrderItem orderItem, Long memberId) {
        Order order = orderItem.getOrder();

        validateOrderOwnedByMember(memberId, order);

        int updatedAmountSum = order.getTotalAmountSum() - orderItem.getEachAmount();
        int updatedPriceSum = order.getTotalPriceSum() - orderItem.getTotalPrice();

        order.updateAmountSumAndPriceSum(updatedAmountSum, updatedPriceSum);

        if (order.getTotalAmountSum() == 0) {
            order.markAsCancelled();
        }
    }

    private void validateOrderOwnedByMember(Long memberId, Order order) {
        boolean isMemberIdDifferent = !order.getMember().getId().equals(memberId);

        if (isMemberIdDifferent) {
            throw new BadRequestException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }
    }
}