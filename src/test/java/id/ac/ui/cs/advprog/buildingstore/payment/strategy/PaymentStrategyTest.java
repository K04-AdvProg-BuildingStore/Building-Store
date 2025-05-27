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
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
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

    private Payment fullPayment;
    private Payment installmentPayment;
    private Integer salesTransactionId = 12345;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        fullPayment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.FULL)
                .method("Credit Card")
                .salesTransactionId(salesTransactionId)
                .build();

        installmentPayment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(50000))
                .status(PaymentStatus.INSTALLMENT)
                .method("Credit Card")
                .salesTransactionId(salesTransactionId)
                .build();
    }

    // Tests for PaymentStrategy Interface
    @Test
    void testFullPaymentStrategySupports() {
        assertTrue(fullPaymentStrategy.supports(PaymentStatus.FULL));
        assertFalse(fullPaymentStrategy.supports(PaymentStatus.INSTALLMENT));
    }

    @Test
    void testInstallmentPaymentStrategySupports() {
        assertTrue(installmentPaymentStrategy.supports(PaymentStatus.INSTALLMENT));
        assertFalse(installmentPaymentStrategy.supports(PaymentStatus.FULL));
    }

    // Tests for FullPaymentStrategy
    @Test
    void testFullPaymentExecuteSuccessful() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(salesTransactionId)).thenReturn(BigDecimal.valueOf(100000));
        doNothing().when(salesTransactionGateway).markAsPaid(salesTransactionId);

        Payment result = fullPaymentStrategy.execute(fullPayment);

        assertNotNull(result);
        assertEquals(fullPayment, result);
        verify(salesTransactionGateway).markAsPaid(salesTransactionId);
    }

    @Test
    void testFullPaymentExecuteTransactionNotFound() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fullPaymentStrategy.execute(fullPayment);
        });

        assertEquals("Sales transaction not found.", exception.getMessage());
        verify(salesTransactionGateway, never()).markAsPaid(any());
    }

    @Test
    void testFullPaymentExecuteTransactionNotPending() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PAID");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            fullPaymentStrategy.execute(fullPayment);
        });

        assertEquals("This transaction is already paid in full.", exception.getMessage());
        verify(salesTransactionGateway, never()).markAsPaid(any());
    }

    @Test
    void testFullPaymentExecuteAmountMismatch() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(salesTransactionId)).thenReturn(BigDecimal.valueOf(150000));

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            fullPaymentStrategy.execute(fullPayment);
        });

        assertTrue(exception.getMessage().startsWith("Full payment must match the exact transaction amount:"));
        verify(salesTransactionGateway, never()).markAsPaid(any());
    }

    // Tests for InstallmentPaymentStrategy
    @Test
    void testInstallmentPaymentExecuteFirstPaymentNotComplete() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(salesTransactionId)).thenReturn(BigDecimal.valueOf(100000));
        when(paymentRepository.findAllBySalesTransactionId(salesTransactionId)).thenReturn(Collections.emptyList());

        Payment result = installmentPaymentStrategy.execute(installmentPayment);

        assertNotNull(result);
        assertEquals(installmentPayment, result);
        verify(salesTransactionGateway, never()).markAsPaid(any());
    }

    @Test
    void testInstallmentPaymentExecuteCompleteWithNewPayment() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(salesTransactionId)).thenReturn(BigDecimal.valueOf(100000));

        Payment previousPayment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(50000))
                .status(PaymentStatus.INSTALLMENT)
                .salesTransactionId(salesTransactionId)
                .build();

        when(paymentRepository.findAllBySalesTransactionId(salesTransactionId)).thenReturn(Arrays.asList(previousPayment));
        doNothing().when(salesTransactionGateway).markAsPaid(salesTransactionId);

        Payment result = installmentPaymentStrategy.execute(installmentPayment);

        assertNotNull(result);
        assertEquals(installmentPayment, result);
        verify(salesTransactionGateway).markAsPaid(salesTransactionId);
    }

    @Test
    void testInstallmentPaymentExecuteExceedingAmount() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PENDING");
        when(salesTransactionGateway.getTotalAmount(salesTransactionId)).thenReturn(BigDecimal.valueOf(100000));

        Payment previousPayment = Payment.builder()
                .id(UUID.randomUUID())
                .amount(BigDecimal.valueOf(70000))
                .status(PaymentStatus.INSTALLMENT)
                .salesTransactionId(salesTransactionId)
                .build();

        when(paymentRepository.findAllBySalesTransactionId(salesTransactionId)).thenReturn(Arrays.asList(previousPayment));
        doNothing().when(salesTransactionGateway).markAsPaid(salesTransactionId);

        Payment result = installmentPaymentStrategy.execute(installmentPayment);

        assertNotNull(result);
        assertEquals(installmentPayment, result);
        verify(salesTransactionGateway).markAsPaid(salesTransactionId);
    }

    @Test
    void testInstallmentPaymentExecuteTransactionNotFound() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(false);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            installmentPaymentStrategy.execute(installmentPayment);
        });

        assertEquals("Sales transaction not found.", exception.getMessage());
        verify(salesTransactionGateway, never()).markAsPaid(any());
    }

    @Test
    void testInstallmentPaymentExecuteTransactionNotPending() {
        when(salesTransactionGateway.exists(salesTransactionId)).thenReturn(true);
        when(salesTransactionGateway.getStatus(salesTransactionId)).thenReturn("PAID");

        IllegalStateException exception = assertThrows(IllegalStateException.class, () -> {
            installmentPaymentStrategy.execute(installmentPayment);
        });

        assertEquals("This transaction is already paid in full.", exception.getMessage());
        verify(salesTransactionGateway, never()).markAsPaid(any());
    }
}
