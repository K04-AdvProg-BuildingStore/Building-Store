package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;

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

        // Check transaction status
        if (status.equalsIgnoreCase("PAID") || status.equalsIgnoreCase("FULL")) {
            throw new IllegalStateException("This transaction is already paid in full.");
        } else if (status.equalsIgnoreCase("PARTIALLY_PAID")) {
            throw new IllegalStateException("This transaction is already partially paid. Please continue with installment payments.");
        }

        // Validate full payment amount
        BigDecimal transactionTotal = salesTransactionService.getTotalAmount(transactionId);
        if (transactionTotal != null && payment.getAmount() != null &&
            payment.getAmount().compareTo(transactionTotal) != 0) {
            throw new IllegalArgumentException("Full payment must match the exact transaction amount: " + transactionTotal);
        }

        salesTransactionService.markAsPaid(transactionId);

        return payment;
    }
}