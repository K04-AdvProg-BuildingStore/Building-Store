package id.ac.ui.cs.advprog.buildingstore.payment.dependency;

import java.math.BigDecimal;

import org.springframework.stereotype.Component;

@Component
public class MockSalesTransactionGateway implements SalesTransactionGateway {

    @Override
    public boolean exists(Integer transactionId) {
        System.out.println("[MOCK] Checking if sales transaction " + transactionId + " exists");
        return true; // Always return true for now
    }

    @Override
    public String getStatus(Integer transactionId) {
        System.out.println("[MOCK] Getting status for transaction: " + transactionId);
        return "PENDING"; // Or "PARTIALLY_PAID"
    }

    @Override
    public void markAsPaid(Integer transactionId) {
        System.out.println("[MOCK] Marking transaction " + transactionId + " as PAID");
    }

    @Override
    public void markAsPartiallyPaid(Integer transactionId) {
        System.out.println("[MOCK] Marking transaction " + transactionId + " as PARTIALLY_PAID");
    }

    @Override
    public BigDecimal getTotalAmount(Integer transactionId) {
        System.out.println("[MOCK] Getting total amount for transaction: " + transactionId);
        return new BigDecimal("1000000"); // Hardcoded dummy value
    }
}
