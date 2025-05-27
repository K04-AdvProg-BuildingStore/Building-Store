package id.ac.ui.cs.advprog.buildingstore.payment.dependency;

import java.math.BigDecimal;

public interface SalesTransactionGateway {
    boolean exists(Integer transactionId);
    String getStatus(Integer transactionId);
    void markAsPaid(Integer transactionId);
    void markAsPartiallyPaid(Integer transactionId);
    BigDecimal getTotalAmount(Integer transactionId);
}
