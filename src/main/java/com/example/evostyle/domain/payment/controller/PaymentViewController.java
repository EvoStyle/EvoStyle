package com.example.evostyle.domain.payment.controller;


import com.example.evostyle.domain.order.repository.OrderRepository;
import com.example.evostyle.domain.payment.dto.response.PaymentCheckoutResponse;
import com.example.evostyle.domain.payment.service.PaymentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller("/api/payment")
@RequestMapping
@RequiredArgsConstructor
public class PaymentViewController {

    private final PaymentService paymentService;

    @GetMapping("/checkout/{orderId}")
    public String checkoutPage(@PathVariable(name = "orderId") Long orderId, Model model) {
        PaymentCheckoutResponse checkoutResponse = paymentService.checkoutPayment(orderId);
        model.addAttribute("orderInfo", checkoutResponse);
        return "payment/checkout";
    }

}
