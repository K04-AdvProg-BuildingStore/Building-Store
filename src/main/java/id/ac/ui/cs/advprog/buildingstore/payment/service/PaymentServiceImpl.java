package id.ac.ui.cs.advprog.buildingstore.payment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Service
@RequiredArgsConstructor
@Slf4j
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment create(Payment payment) {
        // Using template method pattern
        return processPayment(payment);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment update(UUID id, Payment updatedPayment) {
        Payment payment = findById(id);
        if (payment == null) return null;

        payment.setAmount(updatedPayment.getAmount());
        payment.setStatus(updatedPayment.getStatus());
        payment.setMethod(updatedPayment.getMethod());
        payment.setSalesTransactionId(updatedPayment.getSalesTransactionId());

        return paymentRepository.save(payment);
    }

    @Override
    public void delete(UUID id) {
        paymentRepository.deleteById(id);
    }
    
    @Override
    public void validatePayment(Payment payment) {
        log.info("Validating payment of {} using method: {}", 
                payment.getAmount(), payment.getMethod());
        if (payment.getAmount() <= 0) {
            log.error("Invalid payment amount: {}", payment.getAmount());
        }
    }
    
    @Override
    public void logPaymentAttempt(Payment payment) {
        log.info("Payment attempt: {} - {} - {}", 
                payment.getAmount(), 
                payment.getMethod(), 
                payment.getStatus());
    }
    
    @Override
    public Payment executePayment(Payment payment) {
        log.info("Executing payment transaction for {}", payment.getAmount());
        return paymentRepository.save(payment);
    }
    
    @Override
    public void notifyPaymentComplete(Payment payment) {
        log.info("Payment completed: {} with status: {}", 
                payment.getId(), 
                payment.getStatus());
    }
}
