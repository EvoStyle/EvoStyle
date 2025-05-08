package com.example.evostyle.domain.payment.repository;

import com.example.evostyle.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Query("""
            SELECT p
            FROM Payment p JOIN FETCH Order o
            ON p.order.id = o.id
            WHERE o.member.id = :memberId
            
            """)
    List<Payment> findByMemberId(@Param("memberId") Long memberId);

    Optional<Payment> findByPaymentKey(String paymentKey);


}
