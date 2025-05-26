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
    private PaymentMetricService metricService;

    @Mock
    private FullPaymentStrategy fullPaymentStrategy;

    @Mock
    private InstallmentPaymentStrategy installmentPaymentStrategy;

    @Mock
    private SalesTransactionGateway salesTransactionGateway;

    private PaymentService paymentService;
    private Payment testPayment;
    private UUID testId;
    private List<PaymentStrategy> strategies;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        testId = UUID.randomUUID();
        testPayment = Payment.builder()
                .id(testId)
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.FULL)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        strategies = Arrays.asList(fullPaymentStrategy, installmentPaymentStrategy);
        paymentService = new PaymentServiceImpl(paymentRepository, strategies, metricService);

        // Configure mock behaviors
        when(paymentRepository.findById(testId)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Strategy behavior
        when(fullPaymentStrategy.supports(PaymentStatus.FULL)).thenReturn(true);
        when(fullPaymentStrategy.supports(PaymentStatus.INSTALLMENT)).thenReturn(false);
        when(installmentPaymentStrategy.supports(PaymentStatus.FULL)).thenReturn(false);
        when(installmentPaymentStrategy.supports(PaymentStatus.INSTALLMENT)).thenReturn(true);

        when(fullPaymentStrategy.execute(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
        when(installmentPaymentStrategy.execute(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));
    }

    // Happy Path Tests

    @Test
    void create_WithValidPayment_ShouldReturnSavedPayment() {
        Payment result = paymentService.create(testPayment);

        assertNotNull(result);
        assertEquals(testPayment.getAmount(), result.getAmount());
        assertEquals(testPayment.getStatus(), result.getStatus());
        assertEquals(testPayment.getMethod(), result.getMethod());

        verify(metricService).recordPaymentAttempt(testPayment);
        verify(metricService).recordSuccessfulPayment(any(Payment.class));
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void findById_WithExistingId_ShouldReturnPayment() {
        Payment result = paymentService.findById(testId);

        assertNotNull(result);
        assertEquals(testPayment.getId(), result.getId());

        verify(paymentRepository).findById(testId);
    }

    @Test
    void findAll_ShouldReturnAllPayments() {
        List<Payment> expectedPayments = Collections.singletonList(testPayment);
        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        List<Payment> result = paymentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(testPayment, result.get(0));

        verify(paymentRepository).findAll();
    }

    @Test
    void update_WithExistingId_ShouldReturnUpdatedPayment() {
        Payment updatedPayment = Payment.builder()
                .id(testId)
                .amount(BigDecimal.valueOf(200000))
                .status(PaymentStatus.INSTALLMENT)
                .method("Bank Transfer")
                .salesTransactionId(12345)
                .build();

        Payment result = paymentService.update(testId, updatedPayment);

        assertNotNull(result);
        assertEquals(updatedPayment.getAmount(), result.getAmount());
        assertEquals(updatedPayment.getStatus(), result.getStatus());
        assertEquals(updatedPayment.getMethod(), result.getMethod());

        verify(paymentRepository).findById(testId);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void delete_WithExistingId_ShouldDeletePayment() {
        doNothing().when(paymentRepository).deleteById(testId);

        paymentService.delete(testId);

        verify(paymentRepository).deleteById(testId);
    }

    // Template Method Pattern Tests

    @Test
    void processPayment_ShouldFollowTemplateSteps() {
        // Create a spy of the paymentService to verify method calls
        PaymentService serviceSpy = spy(paymentService);

        // Make the spy use the real processPayment method
        doCallRealMethod().when(serviceSpy).processPayment(any(Payment.class));

        // Now set up the spy to return values for the steps
        doNothing().when(serviceSpy).validatePayment(any(Payment.class));
        doNothing().when(serviceSpy).logPaymentAttempt(any(Payment.class));
        when(serviceSpy.executePayment(any(Payment.class))).thenReturn(testPayment);
        doNothing().when(serviceSpy).notifyPaymentComplete(any(Payment.class));

        // Call the template method
        Payment result = serviceSpy.processPayment(testPayment);

        // Verify all steps were called in the right order
        verify(serviceSpy).validatePayment(testPayment);
        verify(serviceSpy).logPaymentAttempt(testPayment);
        verify(serviceSpy).executePayment(testPayment);
        verify(serviceSpy).notifyPaymentComplete(testPayment);
        assertEquals(testPayment, result);
    }

    @Test
    void executePayment_WithFullPayment_ShouldUseFullPaymentStrategy() {
        testPayment.setStatus(PaymentStatus.FULL);

        paymentService.executePayment(testPayment);

        verify(fullPaymentStrategy).execute(testPayment);
        verify(installmentPaymentStrategy, never()).execute(any());
    }

    @Test
    void executePayment_WithInstallmentPayment_ShouldUseInstallmentPaymentStrategy() {
        testPayment.setStatus(PaymentStatus.INSTALLMENT);

        paymentService.executePayment(testPayment);

        verify(installmentPaymentStrategy).execute(testPayment);
        verify(fullPaymentStrategy, never()).execute(any());
    }

    // Unhappy Path Tests

    @Test
    void findById_WithNonExistingId_ShouldReturnNull() {
        UUID nonExistingId = UUID.randomUUID();
        when(paymentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Payment result = paymentService.findById(nonExistingId);

        assertNull(result);
        verify(paymentRepository).findById(nonExistingId);
    }

    @Test
    void update_WithNonExistingId_ShouldReturnNull() {
        UUID nonExistingId = UUID.randomUUID();
        when(paymentRepository.findById(nonExistingId)).thenReturn(Optional.empty());

        Payment result = paymentService.update(nonExistingId, testPayment);

        assertNull(result);
        verify(paymentRepository).findById(nonExistingId);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void create_WithNullPayment_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.create(null);
        });
    }

    @Test
    void validatePayment_WithNegativeAmount_ShouldThrowException() {
        Payment invalidPayment = Payment.builder()
                .amount(BigDecimal.valueOf(-100))
                .status(PaymentStatus.FULL)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.validatePayment(invalidPayment);
        });
    }

    @Test
    void validatePayment_WithNullAmount_ShouldThrowException() {
        Payment invalidPayment = Payment.builder()
                .amount(null)
                .status(PaymentStatus.FULL)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        // Modified to expect IllegalArgumentException instead of NullPointerException
        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.validatePayment(invalidPayment);
        });
    }

    @Test
    void validatePayment_WithNullStatus_ShouldThrowException() {
        Payment invalidPayment = Payment.builder()
                .amount(BigDecimal.valueOf(100000))
                .status(null)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.validatePayment(invalidPayment);
        });
    }

    @Test
    void executePayment_WithUnsupportedPaymentStatus_ShouldThrowException() {
        // Mock behavior for no matching strategy
        when(fullPaymentStrategy.supports(any())).thenReturn(false);
        when(installmentPaymentStrategy.supports(any())).thenReturn(false);

        assertThrows(IllegalArgumentException.class, () -> {
            paymentService.executePayment(testPayment);
        });
    }

    @Test
    void executePayment_WithNoStrategies_ShouldSavePaymentDirectly() {
        // Create a service with empty strategy list
        PaymentService serviceWithNoStrategies = new PaymentServiceImpl(paymentRepository, Collections.emptyList(), metricService);

        Payment result = serviceWithNoStrategies.executePayment(testPayment);

        assertNotNull(result);
        verify(paymentRepository).save(testPayment);
    }

    @Test
    void executePayment_WithNullPayment_ShouldThrowException() {
        PaymentServiceImpl paymentServiceImpl = (PaymentServiceImpl) paymentService;

        assertThrows(NullPointerException.class, () -> {
            paymentServiceImpl.executePayment(null);
        });
    }

    @Test
    void notifyPaymentComplete_ShouldRecordSuccessfulPayment() {
        paymentService.notifyPaymentComplete(testPayment);

        verify(metricService).recordSuccessfulPayment(testPayment);
    }
}