package com.example.payment.service;

import com.example.payment.entity.Payment;
import com.example.payment.entity.PaymentMethod;
import com.example.payment.entity.PaymentStatus;
import com.example.payment.exception.InvalidPaymentException;
import com.example.payment.exception.PaymentNotFoundException;
import com.example.payment.repository.PaymentRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PaymentService {

    private final PaymentRepository repository;

    public PaymentService(PaymentRepository repository) {
        this.repository = repository;
    }

    public Payment createPayment(String customerName, Double amount, PaymentMethod paymentMethod) {
        if (customerName == null || customerName.trim().isEmpty()) {
            throw new InvalidPaymentException("Customer name is required.");
        }
        if (amount == null || amount <= 0) {
            throw new InvalidPaymentException("Amount must be greater than zero.");
        }
        if (paymentMethod == null) {
            throw new InvalidPaymentException("Payment method is required.");
        }

        Payment payment = new Payment();
        payment.setCustomerName(customerName.trim());
        payment.setAmount(amount);
        payment.setPaymentMethod(paymentMethod);
        payment.setStatus(PaymentStatus.PENDING);
        payment.setPaymentDate(LocalDateTime.now());

        return repository.save(payment);
    }

    public List<Payment> getAllPayments() {
        return repository.findAllByOrderByPaymentDateDesc();
    }

    public Payment getPaymentById(Long id) {
        return repository.findById(id)
                .orElseThrow(() -> new PaymentNotFoundException("Payment with ID " + id + " was not found."));
    }

    public Payment processPayment(Long id) {
        Payment payment = getPaymentById(id);

        if (payment.getStatus() != PaymentStatus.PENDING) {
            throw new InvalidPaymentException(
                    "Only PENDING payments can be processed. Current status: " + payment.getStatus());
        }

        // Simple simulation: even amounts succeed, odd amounts fail
        if (payment.getAmount() % 2 == 0) {
            payment.setStatus(PaymentStatus.SUCCESS);
        } else {
            payment.setStatus(PaymentStatus.FAILED);
        }

        return repository.save(payment);
    }

    public Payment refundPayment(Long id) {
        Payment payment = getPaymentById(id);

        if (payment.getStatus() != PaymentStatus.SUCCESS) {
            throw new InvalidPaymentException(
                    "Only SUCCESS payments can be refunded. Current status: " + payment.getStatus());
        }

        payment.setStatus(PaymentStatus.REFUNDED);
        return repository.save(payment);
    }

    // ---- Dashboard helper methods ----

    public long getTotalPayments() {
        return repository.count();
    }

    public long getSuccessfulPayments() {
        return repository.countByStatus(PaymentStatus.SUCCESS);
    }

    public long getFailedPayments() {
        return repository.countByStatus(PaymentStatus.FAILED);
    }

    public double getTotalAmount() {
        return repository.findAll()
                .stream()
                .mapToDouble(Payment::getAmount)
                .sum();
    }
}
