package id.ac.ui.cs.advprog.buildingstore.payment.service;

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
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class PaymentServiceImplTest {

    @Mock
    private PaymentRepository paymentRepository;

    @InjectMocks
    private PaymentServiceImpl paymentService;

    private Payment testPayment;
    private UUID testId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        
        testId = UUID.randomUUID();
        testPayment = Payment.builder()
                .id(testId)
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.PAID)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();
    }

    // Happy Path Tests

    @Test
    void create_WithValidPayment_ShouldSaveAndReturnPayment() {
        when(paymentRepository.save(any(Payment.class))).thenReturn(testPayment);

        Payment result = paymentService.create(testPayment);

        assertNotNull(result);
        assertEquals(testPayment, result);
        verify(paymentRepository).save(testPayment);
    }

    @Test
    void findAll_WithExistingPayments_ShouldReturnAllPayments() {
        List<Payment> expectedPayments = Arrays.asList(testPayment);
        when(paymentRepository.findAll()).thenReturn(expectedPayments);

        List<Payment> result = paymentService.findAll();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals(expectedPayments, result);
        verify(paymentRepository).findAll();
    }
    
    @Test
    void findAll_WithNoPayments_ShouldReturnEmptyList() {
        when(paymentRepository.findAll()).thenReturn(Collections.emptyList());

        List<Payment> result = paymentService.findAll();

        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(paymentRepository).findAll();
    }

    @Test
    void findById_WithExistingPayment_ShouldReturnPayment() {
        when(paymentRepository.findById(testId)).thenReturn(Optional.of(testPayment));

        Payment result = paymentService.findById(testId);

        assertNotNull(result);
        assertEquals(testPayment, result);
        verify(paymentRepository).findById(testId);
    }

    @Test
    void update_WithExistingPayment_ShouldUpdateAndReturnPayment() {
        Payment updatedPayment = Payment.builder()
                .id(testId)
                .amount(BigDecimal.valueOf(200000))
                .status(PaymentStatus.INSTALLMENT)
                .method("Debit Card")
                .salesTransactionId(67890)
                .build();

        when(paymentRepository.findById(testId)).thenReturn(Optional.of(testPayment));
        when(paymentRepository.save(any(Payment.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Payment result = paymentService.update(testId, updatedPayment);

        assertNotNull(result);
        assertEquals(updatedPayment.getAmount(), result.getAmount());
        assertEquals(updatedPayment.getStatus(), result.getStatus());
        assertEquals(updatedPayment.getMethod(), result.getMethod());
        assertEquals(updatedPayment.getSalesTransactionId(), result.getSalesTransactionId());
        verify(paymentRepository).findById(testId);
        verify(paymentRepository).save(any(Payment.class));
    }

    @Test
    void delete_WithExistingId_ShouldCallRepositoryDeleteById() {
        doNothing().when(paymentRepository).deleteById(testId);

        paymentService.delete(testId);

        verify(paymentRepository).deleteById(testId);
    }
    
    // Unhappy Path Tests

    @Test
    void findById_WithNonExistingPayment_ShouldReturnNull() {
        when(paymentRepository.findById(testId)).thenReturn(Optional.empty());

        Payment result = paymentService.findById(testId);

        assertNull(result);
        verify(paymentRepository).findById(testId);
    }

    @Test
    void update_WithNonExistingPayment_ShouldReturnNull() {
        Payment updatedPayment = Payment.builder()
                .amount(BigDecimal.valueOf(200000))
                .status(PaymentStatus.INSTALLMENT)
                .method("Debit Card")
                .salesTransactionId(67890)
                .build();

        when(paymentRepository.findById(testId)).thenReturn(Optional.empty());

        Payment result = paymentService.update(testId, updatedPayment);

        assertNull(result);
        verify(paymentRepository).findById(testId);
        verify(paymentRepository, never()).save(any(Payment.class));
    }

    @Test
    void delete_WithNonExistingId_ShouldNotThrowException() {
        doNothing().when(paymentRepository).deleteById(any());
        
        // Should not throw exception when deleting non-existing ID
        assertDoesNotThrow(() -> paymentService.delete(UUID.randomUUID()));
    }

    @Test
    void create_WithNullPayment_ShouldThrowException() {
        when(paymentRepository.save(null)).thenThrow(IllegalArgumentException.class);
        
        assertThrows(IllegalArgumentException.class, () -> paymentService.create(null));
    }

    @Test
    void update_WithNullPayment_ShouldThrowException() {
        when(paymentRepository.findById(testId)).thenReturn(Optional.of(testPayment));
        
        assertThrows(NullPointerException.class, () -> paymentService.update(testId, null));
    }
}
