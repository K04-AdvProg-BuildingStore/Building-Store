package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.math.BigDecimal;
import java.util.List;

@Component
public class InstallmentPaymentStrategy implements PaymentStrategy {

    @Autowired
    private SalesTransactionGateway salesTransactionService;

    @Autowired
    private PaymentRepository paymentRepository;

    @Override
    public boolean supports(PaymentStatus status) {
        return status == PaymentStatus.INSTALLMENT;
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

        BigDecimal transactionTotal = salesTransactionService.getTotalAmount(transactionId);
        List<Payment> pastPayments = paymentRepository.findAllBySalesTransactionId(transactionId);

        BigDecimal totalPaid = pastPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add)
                .add(payment.getAmount());

        // If total paid amount meets or exceeds transaction total, mark as FULL
        if (totalPaid.compareTo(transactionTotal) >= 0) {
            salesTransactionService.markAsPaid(transactionId);
        }
        // Otherwise, it remains in PENDING status

        return payment;
    }
}