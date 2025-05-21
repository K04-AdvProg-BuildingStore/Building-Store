package id.ac.ui.cs.advprog.buildingstore.payment.dependency;

// Update these imports to match your actual package structure
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model .SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.service.SalesTransactionService;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.Optional;

@Component
public class SalesTransactionGatewayImpl implements SalesTransactionGateway {

    private final SalesTransactionService salesTransactionService;

    public SalesTransactionGatewayImpl(SalesTransactionService salesTransactionService) {
        this.salesTransactionService = salesTransactionService;
    }

    @Override
    public boolean exists(Integer salesTransactionId) {
        return salesTransactionService.findById(salesTransactionId).isPresent();
    }

    @Override
    public String getStatus(Integer salesTransactionId) {
        Optional<SalesTransaction> transaction = salesTransactionService.findById(salesTransactionId);
        return transaction.map(t -> t.getStatus().name()).orElse(null);
    }

    @Override
    public BigDecimal getTotalAmount(Integer salesTransactionId) {
        Optional<SalesTransaction> transaction = salesTransactionService.findById(salesTransactionId);
        if (transaction.isPresent()) {
            return transaction.get().getItems().stream()
                    .map(item -> BigDecimal.valueOf(item.getPrice() * item.getQuantity()))
                    .reduce(BigDecimal.ZERO, BigDecimal::add);
        }
        return BigDecimal.ZERO;
    }

    @Override
    public void markAsPaid(Integer salesTransactionId) {
        updateTransactionStatus(salesTransactionId, TransactionStatus.PAID);
    }

    @Override
    public void markAsPartiallyPaid(Integer salesTransactionId) {
        // For installment payments, the status remains PENDING until fully paid
        updateTransactionStatus(salesTransactionId, TransactionStatus.PENDING);
    }

    private void updateTransactionStatus(Integer salesTransactionId, TransactionStatus status) {
        Optional<SalesTransaction> transactionOptional = salesTransactionService.findById(salesTransactionId);
        if (transactionOptional.isPresent()) {
            SalesTransaction transaction = transactionOptional.get();

            // Adjust this to match your SalesTransactionService implementation
            if (transaction.getStatus() != status) {
                salesTransactionService.updateTransaction(
                    salesTransactionId,
                    transaction.getCashier(),
                    transaction.getCustomerPhone(),
                    status,
                    transaction.getItems()
                );
            }
        } else {
            throw new IllegalArgumentException("Transaction with ID " + salesTransactionId + " not found");
        }
    }
}