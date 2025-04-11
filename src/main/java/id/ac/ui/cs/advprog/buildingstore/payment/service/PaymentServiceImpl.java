package id.ac.ui.cs.advprog.buildingstore.payment.service;

import java.util.List;
import java.util.UUID;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PaymentServiceImpl implements PaymentService {

    private final PaymentRepository paymentRepository;

    @Override
    public Payment create(Payment payment) {
        return paymentRepository.save(payment);
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
}
