package id.ac.ui.cs.advprog.buildingstore.payment.service;

import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
import id.ac.ui.cs.advprog.buildingstore.payment.strategy.FullPaymentStrategy;
import id.ac.ui.cs.advprog.buildingstore.payment.strategy.InstallmentPaymentStrategy;
import id.ac.ui.cs.advprog.buildingstore.payment.strategy.PaymentStrategy;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @Mock
    private SalesTransactionGateway salesTransactionGateway;

    @Mock
    private List<PaymentStrategy> paymentStrategies;

    @Mock
    private FullPaymentStrategy fullPaymentStrategy;

    @Mock
    private InstallmentPaymentStrategy installmentPaymentStrategy;

    @Mock
    private PaymentMetricService metricService;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment fullPayment;
    private Payment installmentPayment;
    private UUID paymentId;
    private Integer salesTransactionId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        paymentId = UUID.randomUUID();
        salesTransactionId = 12345;

        fullPayment = Payment.builder()
                .id(paymentId)
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.FULL)
                .method("Credit Card")
                .salesTransactionId(salesTransactionId)
                .build();

        installmentPayment = Payment.builder()
                .id(paymentId)
                .amount(BigDecimal.valueOf(50000))
                .status(PaymentStatus.INSTALLMENT)
                .method("Debit Card")
                .salesTransactionId(salesTransactionId)
                .build();

        // Set up the service with our mocked strategies
        paymentService = new PaymentServiceImpl(
                paymentRepository,
                Arrays.asList(fullPaymentStrategy, installmentPaymentStrategy),
                metricService);
    }

    @Test
    void testCreateFullPayment() {
        when(fullPaymentStrategy.supports(PaymentStatus.FULL)).thenReturn(true);
        when(fullPaymentStrategy.execute(fullPayment)).thenReturn(fullPayment);
        when(paymentRepository.save(fullPayment)).thenReturn(fullPayment);

        Payment result = paymentService.create(fullPayment);

        assertEquals(fullPayment, result);
        verify(fullPaymentStrategy).execute(fullPayment);
        verify(metricService).recordPaymentAttempt(fullPayment);
        verify(metricService).recordSuccessfulPayment(fullPayment);
        verify(paymentRepository).save(fullPayment);
    }

    @Test
    void testCreateInstallmentPayment() {
        when(installmentPaymentStrategy.supports(PaymentStatus.INSTALLMENT)).thenReturn(true);
        when(installmentPaymentStrategy.execute(installmentPayment)).thenReturn(installmentPayment);
        when(paymentRepository.save(installmentPayment)).thenReturn(installmentPayment);

        Payment result = paymentService.create(installmentPayment);

        assertEquals(installmentPayment, result);
        verify(installmentPaymentStrategy).execute(installmentPayment);
        verify(metricService).recordPaymentAttempt(installmentPayment);
        verify(metricService).recordSuccessfulPayment(installmentPayment);
        verify(paymentRepository).save(installmentPayment);
    }

    @Test
    void testFindById() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(fullPayment));

        Payment result = paymentService.findById(paymentId);

        assertEquals(fullPayment, result);
    }

    @Test
    void testFindByIdNotFound() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        Payment result = paymentService.findById(paymentId);

        assertNull(result);
    }

    @Test
    void testFindAll() {
        List<Payment> expectedPayments = Arrays.asList(fullPayment, installmentPayment);
        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        List<Payment> result = paymentService.findAll();

        assertEquals(expectedPayments, result);
    }

    @Test
    void testUpdate() {
        Payment updatedPayment = Payment.builder()
                .id(paymentId)
                .amount(BigDecimal.valueOf(150000))
                .status(PaymentStatus.FULL)
                .method("Updated Method")
                .salesTransactionId(salesTransactionId)
                .build();

        when(paymentRepository.findById(paymentId)).thenReturn(Optional.of(fullPayment));
        when(paymentRepository.save(any(Payment.class))).thenReturn(updatedPayment);

        Payment result = paymentService.update(paymentId, updatedPayment);

        assertEquals(updatedPayment, result);
        assertEquals(paymentId, result.getId()); // Ensure ID remains the same
    }

    @Test
    void testUpdateNotFound() {
        when(paymentRepository.findById(paymentId)).thenReturn(Optional.empty());

        Payment result = paymentService.update(paymentId, fullPayment);

        assertNull(result);
        verify(paymentRepository, never()).save(any());
    }

    @Test
    void testDelete() {
        doNothing().when(paymentRepository).deleteById(paymentId);

        paymentService.delete(paymentId);

        verify(paymentRepository).deleteById(paymentId);
    }
}
