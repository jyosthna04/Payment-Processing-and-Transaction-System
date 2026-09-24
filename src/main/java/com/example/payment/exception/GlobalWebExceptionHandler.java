package com.example.payment.exception;

import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice(basePackages = "com.example.payment.controller")
public class GlobalWebExceptionHandler {

    @ExceptionHandler(PaymentNotFoundException.class)
    public String handleNotFound(PaymentNotFoundException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public String handleInvalid(InvalidPaymentException ex, Model model) {
        model.addAttribute("error", ex.getMessage());
        return "error";
    }
}
