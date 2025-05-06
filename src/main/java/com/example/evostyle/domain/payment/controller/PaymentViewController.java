package com.example.evostyle.domain.payment.controller;

import com.example.evostyle.domain.payment.dto.response.PaymentCheckoutResponse;
import com.example.evostyle.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PaymentViewController {

    private final PaymentService paymentService;

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "payment/checkout";
    }

}
