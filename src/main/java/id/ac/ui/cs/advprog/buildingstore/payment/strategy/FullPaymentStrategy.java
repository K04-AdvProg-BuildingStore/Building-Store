package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class FullPaymentStrategy implements PaymentStrategy {

    @Autowired
    private SalesTransactionGateway salesTransactionService;

    @Override
    public boolean supports(PaymentStatus status) {
        return status == PaymentStatus.FULL;
    }

    @Override
    public Payment execute(Payment payment) {
        Integer transactionId = payment.getSalesTransactionId();

        if (!salesTransactionService.exists(transactionId)) {
            throw new IllegalArgumentException("Sales transaction not found.");
        }

        String status = salesTransactionService.getStatus(transactionId);

        // Check that transaction is in PENDING status
        if (!status.equalsIgnoreCase("PENDING")) {
            throw new IllegalStateException("Payments can only be made to PENDING transactions.");
        }

        salesTransactionService.markAsPaid(transactionId);

        return payment;
    }
}