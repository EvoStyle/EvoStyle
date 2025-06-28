package com.example.evostyle.domain.member.service;

import com.example.evostyle.domain.member.dto.request.UpdateMemberRequest;
import com.example.evostyle.domain.member.dto.response.MemberResponse;
import com.example.evostyle.domain.member.entity.Member;
import com.example.evostyle.domain.member.repository.MemberRepository;
import com.example.evostyle.domain.order.entity.Order;
import com.example.evostyle.domain.order.repository.OrderItemRepository;
import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.global.exception.ErrorCode;
import com.example.evostyle.global.exception.ForbiddenException;
import com.example.evostyle.global.exception.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class MemberService {

    private final MemberRepository memberRepository;
    private final OrderItemRepository orderItemRepository;
    private final OrderRepository orderRepository;

    public MemberResponse readMember(Long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        return MemberResponse.from(member);
    }

    @Transactional
    public MemberResponse updateMember(UpdateMemberRequest request, Long memberId) {
        Member member = memberRepository.findByIdAndIsDeletedFalse(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        member.updateMember(request.nickname(), request.age(), request.phoneNumber());

        return MemberResponse.from(member);
    }

    @Transactional
    public void deleteMember(Long memberId, Long loginMemberId) {
        Member member = memberRepository.findById(memberId)
            .orElseThrow(() -> new NotFoundException(ErrorCode.MEMBER_NOT_FOUND));

        if (!member.getId().equals(loginMemberId)) {
            throw new ForbiddenException(ErrorCode.FORBIDDEN_MEMBER_OPERATION);
        }

        member.deleteMember();
    }

    @Transactional
    public void increasePurchaseSum(Long orderId) {
        Order order = orderRepository.findOrderWithDetails(orderId);
        order.getMember().increasePurchaseSum(order.getTotalPriceSum());
    }

    @Transactional
    public void decreasePurchaseSum(Long orderId) {

        Order order = orderRepository.findOrderWithDetails(orderId);
        order.getMember().decreasePurchaseSum(order.getTotalPriceSum());
    }
}
