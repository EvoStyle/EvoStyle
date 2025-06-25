package com.example.evostyle.domain.payment.service;


import com.example.evostyle.domain.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class PaymentCleanupScheduler {

    private final PaymentRepository paymentRepository;

    @Scheduled(cron = "0 0 2 * * *")
    public void cleanupAbandonedPayments(){
        paymentRepository.deletePayment();
    }
}
