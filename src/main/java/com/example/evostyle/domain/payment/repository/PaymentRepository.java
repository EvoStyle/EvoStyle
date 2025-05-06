package com.example.evostyle.domain.payment.repository;

import com.example.evostyle.domain.payment.entity.Payment;
import org.springframework.data.jpa.repository.JpaRepository;

public interface PaymentRepository extends JpaRepository<Payment, Long> {
}
