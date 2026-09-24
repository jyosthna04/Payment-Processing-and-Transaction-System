package com.example.payment.controller;

import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentMethod;
import com.example.payment.entity.PaymentStatus;
import com.example.payment.exception.InvalidPaymentException;
import com.example.payment.exception.PaymentNotFoundException;
import com.example.payment.service.PaymentService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/payments")
public class PaymentWebController {

    private final PaymentService service;

    public PaymentWebController(PaymentService service) {
        this.service = service;
    }

    @GetMapping
    public String listPayments(Model model) {
        model.addAttribute("payments", service.getAllPayments());
        return "payments";
    }

    @GetMapping("/new")
    public String showCreateForm(Model model) {
        model.addAttribute("payment", new Payment());
        model.addAttribute("methods", PaymentMethod.values());
        return "create-payment";
    }

    @PostMapping
    public String createPayment(@RequestParam String customerName,
                                @RequestParam Double amount,
                                @RequestParam PaymentMethod paymentMethod,
                                Model model) {
        try {
            Payment payment = service.createPayment(customerName, amount, paymentMethod);
            return "redirect:/payments/" + payment.getId();
        } catch (InvalidPaymentException ex) {
            model.addAttribute("error", ex.getMessage());
            model.addAttribute("methods", PaymentMethod.values());
            model.addAttribute("payment", new Payment());
            return "create-payment";
        }
    }

    @GetMapping("/{id}")
    public String paymentDetails(@PathVariable Long id, Model model) {
        try {
            Payment payment = service.getPaymentById(id);
            model.addAttribute("payment", payment);
            return "payment-detail";
        } catch (PaymentNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/process")
    public String processPayment(@PathVariable Long id, Model model) {
        try {
            service.processPayment(id);
            return "redirect:/payments/" + id;
        } catch (InvalidPaymentException | PaymentNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }

    @PostMapping("/{id}/refund")
    public String refundPayment(@PathVariable Long id, Model model) {
        try {
            service.refundPayment(id);
            return "redirect:/payments/" + id;
        } catch (InvalidPaymentException | PaymentNotFoundException ex) {
            model.addAttribute("error", ex.getMessage());
            return "error";
        }
    }
}
