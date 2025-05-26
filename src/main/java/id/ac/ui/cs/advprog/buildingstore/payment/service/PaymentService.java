package id.ac.ui.cs.advprog.buildingstore.payment.service;

import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;

public interface PaymentService {
    Payment create(Payment payment);
    List<Payment> findAll();
    Payment findById(UUID id);
    Payment update(UUID id, Payment payment);
    void delete(UUID id);
    
    // Template method pattern steps
    default Payment processPayment(Payment payment) {
        validatePayment(payment);
        logPaymentAttempt(payment);
        Payment result = executePayment(payment);
        notifyPaymentComplete(result);
        return result;
    }
    
    // Steps to be implemented by concrete classes
    void validatePayment(Payment payment);
    void logPaymentAttempt(Payment payment);
    Payment executePayment(Payment payment);
    void notifyPaymentComplete(Payment payment);
}
