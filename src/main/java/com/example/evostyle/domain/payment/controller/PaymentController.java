package com.example.evostyle.domain.payment.controller;


import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.service.PaymentService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {
    private final PaymentService paymentService;

    @PostMapping("/payments/confirm/{orderId}")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody PaymentConfirmRequest request,
                                                          @PathVariable(name = "orderId")Long orderId) {
        PaymentResponse paymentResponse = paymentService.confirmPayment(request, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }

    @DeleteMapping("/payments/refund")
    public ResponseEntity<PaymentCancelResponse> refundPayment(@RequestBody PaymentCancelRequest request,
                                                               @AuthenticationPrincipal AuthUser authUser){

        PaymentCancelResponse cancelResponse = paymentService.cancelPayment(authUser.memberId(), request);
        return ResponseEntity.ok(cancelResponse);
    }

}