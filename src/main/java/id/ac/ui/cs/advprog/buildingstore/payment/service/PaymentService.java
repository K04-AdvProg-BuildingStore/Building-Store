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
}
