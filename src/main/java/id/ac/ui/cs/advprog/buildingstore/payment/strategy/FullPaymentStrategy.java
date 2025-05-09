package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;

@Component
public class FullPaymentStrategy implements PaymentStrategy {

    @Autowired
    private SalesTransactionGateway salesTransactionService;

    @Override
    public boolean supports(PaymentStatus status) {
        return status == PaymentStatus.PAID;
    }

    @Override
    public Payment execute(Payment payment) {
        Integer transactionId = payment.getSalesTransactionId();

        if (!salesTransactionService.exists(transactionId)) {
            throw new IllegalArgumentException("Sales transaction not found.");
        }

        if (salesTransactionService.getStatus(transactionId).equalsIgnoreCase("PAID")) {
            throw new IllegalStateException("This transaction is already paid in full.");
        }

        salesTransactionService.markAsPaid(transactionId);

        return payment;
    }
}
