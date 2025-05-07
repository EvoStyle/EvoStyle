package com.example.evostyle.domain.payment.controller;


import com.example.evostyle.domain.payment.dto.request.PaymentCancelRequest;
import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentCancelResponse;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.service.PaymentManager;
import com.example.evostyle.domain.payment.service.PaymentService;
import com.example.evostyle.global.security.AuthUser;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;
    private final PaymentManager paymentManager;

    @PostMapping("/payments/confirm/{orderId}")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody PaymentConfirmRequest request,
                                                          @PathVariable(name = "orderId") Long orderId) {
        PaymentResponse paymentResponse = paymentManager.confirmPayment(request, orderId);
        return ResponseEntity.status(HttpStatus.CREATED).body(paymentResponse);
    }

    @DeleteMapping("payments/cancel/{paymentKey}")
    public ResponseEntity<PaymentCancelResponse> paymentCancel(@PathVariable(name = "paymentKey")String paymentKey, @RequestBody PaymentCancelRequest request){
        PaymentCancelResponse response = paymentManager.cancelPayment(request, paymentKey);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }

    @GetMapping("/payments")
    public ResponseEntity<List<PaymentResponse>> findByMember(@AuthenticationPrincipal AuthUser authUser) {
        Long memberId = authUser.memberId();
        List<PaymentResponse> responseList = paymentService.findByMember(memberId);
        return ResponseEntity.status(HttpStatus.OK).body(responseList);
    }


    @GetMapping("/payment/{paymentKey}")
    public ResponseEntity<PaymentResponse> findByPaymentKey(@PathVariable("paymentKey") String paymentKey){
        PaymentResponse response = paymentService.findByPaymentKey(paymentKey);
        return ResponseEntity.status(HttpStatus.OK).body(response);
    }
}