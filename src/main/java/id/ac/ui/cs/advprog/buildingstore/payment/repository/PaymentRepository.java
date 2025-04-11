package id.ac.ui.cs.advprog.buildingstore.payment.repository;

import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;

public interface PaymentRepository extends JpaRepository<Payment, UUID> {
}
