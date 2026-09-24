package com.example.payment.controller;

import com.example.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class DashboardController {

    private final PaymentService service;

    public DashboardController(PaymentService service) {
        this.service = service;
    }

    @GetMapping("/")
    public String dashboard(Model model) {
        model.addAttribute("totalPayments", service.getTotalPayments());
        model.addAttribute("successfulPayments", service.getSuccessfulPayments());
        model.addAttribute("failedPayments", service.getFailedPayments());
        model.addAttribute("totalAmount", service.getTotalAmount());
        return "dashboard";
    }
}
