package com.example.evostyle.domain.payment.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller()
@RequestMapping("/api/auth")
@RequiredArgsConstructor
public class PaymentViewController {

    @GetMapping("/checkout")
    public String checkoutPage() {
        return "payment/checkout";
    }

}
