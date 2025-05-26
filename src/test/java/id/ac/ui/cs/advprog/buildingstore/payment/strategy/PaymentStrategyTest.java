package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;

class PaymentStrategyTest {

    @Mock
    private SalesTransactionGateway salesTransactionGateway;

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private FullPaymentStrategy fullPaymentStrategy;

    @InjectMocks
    private InstallmentPaymentStrategy installmentPaymentStrategy;

    private Payment payment;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        payment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100000))
                .method("Credit Card")
                .salesTransactionId(1234)
                .build();
    }

    // FullPaymentStrategy Tests

    @Test
    void fullPaymentStrategy_supports_ShouldReturnTrueWhenStatusIsFull() {
        assertTrue(fullPaymentStrategy.supports(PaymentStatus.FULL));
        assertFalse(fullPaymentStrategy.supports(PaymentStatus.INSTALLMENT));
    }

    @Test
    void fullPaymentStrategy_execute_ShouldMarkTransactionAsPaid() {
        // Setup
        payment.setStatus(PaymentStatus.FULL);
        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(anyInt())).thenReturn(BigDecimal.valueOf(100000));
        doNothing().when(salesTransactionGateway).markAsPaid(anyInt());

        // Execute
        Payment result = fullPaymentStrategy.execute(payment);

        // Verify
        assertEquals(payment, result);
        verify(salesTransactionGateway).markAsPaid(payment.getSalesTransactionId());
        verify(salesTransactionGateway, never()).markAsPartiallyPaid(anyInt());
    }

    @Test
    void fullPaymentStrategy_execute_ShouldThrowExceptionWhenTransactionNotFound() {
        // Setup
        payment.setStatus(PaymentStatus.FULL);
        when(salesTransactionGateway.exists(anyInt())).thenReturn(false);

        // Execute and verify
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fullPaymentStrategy.execute(payment);
        });
        assertEquals("Sales transaction not found.", exception.getMessage());
        verify(salesTransactionGateway, never()).markAsPaid(anyInt());
    }

    @Test
    void fullPaymentStrategy_execute_ShouldThrowExceptionWhenTransactionAlreadyPaid() {
        // Setup
        payment.setStatus(PaymentStatus.FULL);
        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PAID");

        // Execute and verify
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            fullPaymentStrategy.execute(payment);
        });
        assertEquals("This transaction is already paid in full.", exception.getMessage());
        verify(salesTransactionGateway, never()).markAsPaid(anyInt());
    }

    @Test
    void fullPaymentStrategy_execute_ShouldThrowExceptionWhenTransactionPartiallyPaid() {
        // Setup
        payment.setStatus(PaymentStatus.FULL);
        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PARTIALLY_PAID");

        // Execute and verify
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            fullPaymentStrategy.execute(payment);
        });
        assertEquals("This transaction is already partially paid. Please continue with installment payments.", exception.getMessage());
    }

    @Test
    void fullPaymentStrategy_execute_ShouldThrowExceptionWhenAmountDoesNotMatch() {
        // Setup
        payment.setStatus(PaymentStatus.FULL);
        payment.setAmount(BigDecimal.valueOf(90000));  // Different from transaction total
        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(anyInt())).thenReturn(BigDecimal.valueOf(100000));

        // Execute and verify
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fullPaymentStrategy.execute(payment);
        });
        assertTrue(exception.getMessage().contains("Full payment must match the exact transaction amount"));
    }

    // InstallmentPaymentStrategy Tests

    @Test
    void installmentPaymentStrategy_supports_ShouldReturnTrueWhenStatusIsInstallment() {
        assertTrue(installmentPaymentStrategy.supports(PaymentStatus.INSTALLMENT));
        assertFalse(installmentPaymentStrategy.supports(PaymentStatus.FULL));
    }

    @Test
    void installmentPaymentStrategy_execute_ShouldMarkAsPartiallyPaidWhenNotFullyPaid() {
        // Setup
        payment.setStatus(PaymentStatus.INSTALLMENT);
        payment.setAmount(BigDecimal.valueOf(30000));
        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(anyInt())).thenReturn(BigDecimal.valueOf(100000));
        when(paymentRepository.findAllBySalesTransactionId(anyInt())).thenReturn(new ArrayList<>());
        doNothing().when(salesTransactionGateway).markAsPartiallyPaid(anyInt());

        // Execute
        Payment result = installmentPaymentStrategy.execute(payment);

        // Verify
        assertEquals(payment, result);
        verify(salesTransactionGateway).markAsPartiallyPaid(payment.getSalesTransactionId());
        verify(salesTransactionGateway, never()).markAsPaid(anyInt());
    }

    @Test
    void installmentPaymentStrategy_execute_ShouldMarkAsPaidWhenFullyPaid() {
        // Setup
        payment.setStatus(PaymentStatus.INSTALLMENT);
        payment.setAmount(BigDecimal.valueOf(40000));

        Payment previousPayment = Payment.builder()
                .amount(BigDecimal.valueOf(60000))
                .status(PaymentStatus.INSTALLMENT)
                .salesTransactionId(payment.getSalesTransactionId())
                .build();

        List<Payment> previousPayments = List.of(previousPayment);

        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(anyInt())).thenReturn(BigDecimal.valueOf(100000));
        when(paymentRepository.findAllBySalesTransactionId(anyInt())).thenReturn(previousPayments);
        doNothing().when(salesTransactionGateway).markAsPaid(anyInt());

        // Execute
        Payment result = installmentPaymentStrategy.execute(payment);

        // Verify
        assertEquals(payment, result);
        verify(salesTransactionGateway).markAsPaid(payment.getSalesTransactionId());
        verify(salesTransactionGateway, never()).markAsPartiallyPaid(anyInt());
    }

    @Test
    void installmentPaymentStrategy_execute_ShouldThrowExceptionWhenTransactionNotFound() {
        // Setup
        payment.setStatus(PaymentStatus.INSTALLMENT);
        when(salesTransactionGateway.exists(anyInt())).thenReturn(false);

        // Execute and verify
        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            installmentPaymentStrategy.execute(payment);
        });
        assertEquals("Sales transaction not found.", exception.getMessage());
    }

    @Test
    void installmentPaymentStrategy_execute_ShouldThrowExceptionWhenTransactionAlreadyPaid() {
        // Setup
        payment.setStatus(PaymentStatus.INSTALLMENT);
        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PAID");

        // Execute and verify
        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            installmentPaymentStrategy.execute(payment);
        });
        assertEquals("This transaction is already paid in full.", exception.getMessage());
    }

    @Test
    void installmentPaymentStrategy_execute_ShouldHandleOverpayment() {
        // Setup - total amount paid exceeds transaction total
        payment.setStatus(PaymentStatus.INSTALLMENT);
        payment.setAmount(BigDecimal.valueOf(50000));

        Payment previousPayment = Payment.builder()
                .amount(BigDecimal.valueOf(60000))
                .status(PaymentStatus.INSTALLMENT)
                .salesTransactionId(payment.getSalesTransactionId())
                .build();

        List<Payment> previousPayments = List.of(previousPayment);

        when(salesTransactionGateway.exists(anyInt())).thenReturn(true);
        when(salesTransactionGateway.getStatus(anyInt())).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(anyInt())).thenReturn(BigDecimal.valueOf(100000));
        when(paymentRepository.findAllBySalesTransactionId(anyInt())).thenReturn(previousPayments);
        doNothing().when(salesTransactionGateway).markAsPaid(anyInt());

        // Execute
        Payment result = installmentPaymentStrategy.execute(payment);

        // Verify transaction is marked as PAID even with overpayment
        assertEquals(payment, result);
        verify(salesTransactionGateway).markAsPaid(payment.getSalesTransactionId());
        verify(salesTransactionGateway, never()).markAsPartiallyPaid(anyInt());
    }
}