package com.example.evostyle.domain.payment.repository;

import com.example.evostyle.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.Optional;

public interface PaymentRepository extends JpaRepository<Payment, Long> {


    @Query("""
            SELECT DISTINCT p
            FROM Payment p
            JOIN FETCH p.order o
            JOIN FETCH o.orderItemList i
            WHERE p.paymentKey = :paymentKey
            """)
    Optional<Payment> findByPaymentKey(@Param("paymentKey") String paymentKey);

    Optional<Payment> findByOrderId(Long orderId);


    @Modifying
    @Query("DELETE FROM Payment p WHERE p.approvedAt = null")
    void deletePayment();


}
