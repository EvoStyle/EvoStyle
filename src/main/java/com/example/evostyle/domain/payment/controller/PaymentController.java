package com.example.evostyle.domain.payment.controller;


import com.example.evostyle.domain.payment.dto.request.PaymentConfirmRequest;
import com.example.evostyle.domain.payment.dto.response.PaymentResponse;
import com.example.evostyle.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/payment")
@RequiredArgsConstructor
public class PaymentController {

    private final PaymentService paymentService;

    @PostMapping("/confirm/{orderId}")
    public ResponseEntity<PaymentResponse> confirmPayment(@RequestBody PaymentConfirmRequest request,
                                                          @PathVariable(name = "orderId") Long orderId) {
        PaymentResponse paymentResponse = paymentService.confirmPayment(request, orderId);
        return ResponseEntity.status(HttpStatus.OK).body(paymentResponse);
    }

//    @GetMapping
//    public void findPayment(){
//
//    }
//}
}