package com.example.payment.controller;

import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentMethod;
import com.example.payment.exception.InvalidPaymentException;
import com.example.payment.exception.PaymentNotFoundException;
import com.example.payment.service.PaymentService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/payments")
public class PaymentApiController {

    private final PaymentService service;

    public PaymentApiController(PaymentService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<Payment> createPayment(@RequestBody Map<String, Object> request) {
        String customerName = (String) request.get("customerName");
        Double amount = Double.valueOf(request.get("amount").toString());
        PaymentMethod method = PaymentMethod.valueOf(request.get("paymentMethod").toString().toUpperCase());

        Payment payment = service.createPayment(customerName, amount, method);
        return new ResponseEntity<>(payment, HttpStatus.CREATED);
    }

    @GetMapping
    public ResponseEntity<List<Payment>> getAllPayments() {
        return ResponseEntity.ok(service.getAllPayments());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Payment> getPayment(@PathVariable Long id) {
        return ResponseEntity.ok(service.getPaymentById(id));
    }

    @PutMapping("/{id}/process")
    public ResponseEntity<Payment> processPayment(@PathVariable Long id) {
        return ResponseEntity.ok(service.processPayment(id));
    }

    @PutMapping("/{id}/refund")
    public ResponseEntity<Payment> refundPayment(@PathVariable Long id) {
        return ResponseEntity.ok(service.refundPayment(id));
    }

    // ---- Exception handlers ----

    @ExceptionHandler(PaymentNotFoundException.class)
    public ResponseEntity<String> handleNotFound(PaymentNotFoundException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.NOT_FOUND);
    }

    @ExceptionHandler(InvalidPaymentException.class)
    public ResponseEntity<String> handleInvalid(InvalidPaymentException ex) {
        return new ResponseEntity<>(ex.getMessage(), HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<String> handleIllegal(IllegalArgumentException ex) {
        return new ResponseEntity<>("Invalid request: " + ex.getMessage(), HttpStatus.BAD_REQUEST);
    }
}
