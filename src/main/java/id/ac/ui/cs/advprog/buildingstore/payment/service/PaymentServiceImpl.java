package id.ac.ui.cs.advprog.buildingstore.payment.service;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.strategy.PaymentStrategy;
import jakarta.persistence.EntityNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PaymentServiceImpl implements PaymentService {

    private final Logger log = LoggerFactory.getLogger(PaymentServiceImpl.class);
    private final PaymentRepository paymentRepository;
    private final List<PaymentStrategy> paymentStrategies;
    private final PaymentMetricService metricService;
    private final SalesTransactionGateway salesTransactionGateway;

    @Autowired
    public PaymentServiceImpl(
            PaymentRepository paymentRepository,
            List<PaymentStrategy> paymentStrategies,
            PaymentMetricService metricService, SalesTransactionGateway salesTransactionGateway) {
        this.paymentRepository = paymentRepository;
        this.paymentStrategies = paymentStrategies != null ? paymentStrategies : new ArrayList<>();
        this.metricService = metricService;
        this.salesTransactionGateway = salesTransactionGateway;
    }

    @Override
    public Payment create(Payment payment) {
        if (payment == null) {
            throw new IllegalArgumentException("Payment cannot be null");
        }
        // Using template method pattern
        return processPayment(payment);
    }

    @Override
    public List<Payment> findAll() {
        return paymentRepository.findAll();
    }

    @Override
    public Payment findById(UUID id) {
        return paymentRepository.findById(id).orElse(null);
    }

    @Override
    public Payment update(UUID id, Payment updatedPayment) {
        Payment payment = findById(id);
        if (payment == null) return null;

        payment.setAmount(updatedPayment.getAmount());
        payment.setStatus(updatedPayment.getStatus());
        payment.setMethod(updatedPayment.getMethod());
        payment.setSalesTransactionId(updatedPayment.getSalesTransactionId());

        return paymentRepository.save(payment);
    }

    @Override
    @Transactional
    public void delete(UUID id) {
        Payment payment = findById(id);
        Integer transactionId = payment.getSalesTransactionId();

        // Delete the payment
        paymentRepository.deleteById(id);

        // Recalculate the total amount paid for this transaction
        List<Payment> remainingPayments = paymentRepository.findAllBySalesTransactionId(transactionId);
        BigDecimal totalPaid = remainingPayments.stream()
                .map(Payment::getAmount)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        BigDecimal totalAmount = salesTransactionGateway.getTotalAmount(transactionId);

        // If there are no remaining payments, reset status to PENDING
        if (remainingPayments.isEmpty()) {
            salesTransactionGateway.markAsPartiallyPaid(transactionId);
        }
        // If total paid is still equal to total amount, keep as PAID
        else if (totalPaid.compareTo(totalAmount) >= 0) {
            salesTransactionGateway.markAsPaid(transactionId);
        }
        // Otherwise it's partially paid
        else {
            salesTransactionGateway.markAsPartiallyPaid(transactionId);
        }
    }

    @Override
    public void validatePayment(Payment payment) {
        log.info("Validating payment of {} using method: {}",
                payment.getAmount(), payment.getMethod());
        if (payment.getAmount() == null || payment.getAmount().compareTo(BigDecimal.ZERO) <= 0) {
            throw new IllegalArgumentException("Amount must be positive.");
        }

        // Validate payment status is set
        if (payment.getStatus() == null) {
            throw new IllegalArgumentException("Payment status must be specified.");
        }
    }

    @Override
    public void logPaymentAttempt(Payment payment) {
        log.info("Payment attempt: {} - {} - {}",
                payment.getAmount(),
                payment.getMethod(),
                payment.getStatus());

        metricService.recordPaymentAttempt(payment);
    }

    @Override
    public Payment executePayment(Payment payment) {
        log.info("Executing payment transaction for {}", payment.getAmount());

        if (paymentStrategies == null || paymentStrategies.isEmpty()) {
            return paymentRepository.save(payment);
        }

        // Find the appropriate strategy for this payment
        PaymentStrategy strategy = paymentStrategies.stream()
                .filter(s -> s.supports(payment.getStatus()))
                .findFirst()
                .orElseThrow(() -> new IllegalArgumentException("No strategy found for payment status: " + payment.getStatus()));

        // Execute the payment using the selected strategy
        Payment processedPayment = strategy.execute(payment);

        // Save the payment to the repository
        return paymentRepository.save(processedPayment);
    }

    @Override
    public void notifyPaymentComplete(Payment payment) {
        log.info("Payment completed: {} with status: {}",
                payment.getId(),
                payment.getStatus());

        metricService.recordSuccessfulPayment(payment);
    }
}