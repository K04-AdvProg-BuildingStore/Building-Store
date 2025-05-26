package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;

public interface PaymentStrategy {
    boolean supports(PaymentStatus status);

    Payment execute(Payment payment);
}
