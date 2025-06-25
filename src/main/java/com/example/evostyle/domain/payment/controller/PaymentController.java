package com.example.evostyle.domain.payment.controller;


import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.service.PaymentManager;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentManager paymentManager;

    @PostMapping("/payments/confirm/{orderId}")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody PaymentConfirmRequest request,
                                                          @PathVariable(name = "orderId")Long orderId) {
        PaymentResponse paymentResponse = paymentManager.handlePaymentConfirmationFlow(request, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }

    @DeleteMapping("/payments/refund")
    public ResponseEntity<PaymentCancelResponse> refundPayment(@RequestBody PaymentCancelRequest request){

        PaymentCancelResponse cancelResponse = paymentManager.handlePaymentCancelFlow(request);
        return ResponseEntity.ok(cancelResponse);
    }

}