package id.ac.ui.cs.advprog.buildingstore.payment.controller;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import id.ac.ui.cs.advprog.buildingstore.payment.service.PaymentService;

class PaymentControllerTest {

    @Mock
    private PaymentService paymentService;

    @InjectMocks
    private PaymentController paymentController;

    private Payment testPayment;
    private UUID testId;

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
    }

    // Happy Path Tests
    
    @Test
    void createPayment_ShouldReturnCreatedPayment() {
        when(paymentService.create(any(Payment.class))).thenReturn(testPayment);

        ResponseEntity<Payment> response = paymentController.createPayment(testPayment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPayment, response.getBody());
        verify(paymentService).create(testPayment);
    }

    @Test
    void getAllPayments_WithPayments_ShouldReturnAllPayments() {
        List<Payment> expectedPayments = Arrays.asList(testPayment);
        when(paymentService.findAll()).thenReturn(expectedPayments);

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(expectedPayments, response.getBody());
        verify(paymentService).findAll();
    }
    
    @Test
    void getAllPayments_WithNoPayments_ShouldReturnEmptyList() {
        when(paymentService.findAll()).thenReturn(Collections.emptyList());

        ResponseEntity<List<Payment>> response = paymentController.getAllPayments();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(0, response.getBody().size());
        verify(paymentService).findAll();
    }

    @Test
    void getPayment_WithExistingId_ShouldReturnPayment() {
        when(paymentService.findById(testId)).thenReturn(testPayment);

        ResponseEntity<Payment> response = paymentController.getPayment(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPayment, response.getBody());
        verify(paymentService).findById(testId);
    }

    @Test
    void updatePayment_WithExistingId_ShouldReturnUpdatedPayment() {
        when(paymentService.update(eq(testId), any(Payment.class))).thenReturn(testPayment);

        ResponseEntity<Payment> response = paymentController.updatePayment(testId, testPayment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(testPayment, response.getBody());
        verify(paymentService).update(testId, testPayment);
    }

    @Test
    void deletePayment_ShouldReturnNoContent() {
        doNothing().when(paymentService).delete(testId);

        ResponseEntity<Void> response = paymentController.deletePayment(testId);

        assertEquals(HttpStatus.NO_CONTENT, response.getStatusCode());
        assertNull(response.getBody());
        verify(paymentService).delete(testId);
    }
    
    // Unhappy Path Tests
    
    @Test
    void getPayment_WithNonExistingId_ShouldReturnNull() {
        when(paymentService.findById(testId)).thenReturn(null);

        ResponseEntity<Payment> response = paymentController.getPayment(testId);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(paymentService).findById(testId);
    }
    
    @Test
    void updatePayment_WithNonExistingId_ShouldReturnNull() {
        when(paymentService.update(eq(testId), any(Payment.class))).thenReturn(null);

        ResponseEntity<Payment> response = paymentController.updatePayment(testId, testPayment);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNull(response.getBody());
        verify(paymentService).update(testId, testPayment);
    }
    
    @Test
    void createPayment_WithNullPayment_ShouldHandleGracefully() {
        Payment nullPayment = null;
        when(paymentService.create(nullPayment)).thenThrow(NullPointerException.class);

        assertThrows(NullPointerException.class, () -> {
            paymentController.createPayment(nullPayment);
        });
    }
}
