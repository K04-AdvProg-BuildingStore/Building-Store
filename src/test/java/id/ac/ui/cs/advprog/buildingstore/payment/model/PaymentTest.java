package id.ac.ui.cs.advprog.buildingstore.payment.model;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class PaymentTest {

    private UUID id;
    private BigDecimal amount;
    private PaymentStatus status;
    private String method;
    private Integer salesTransactionId;
    private LocalDateTime createdAt;

    @BeforeEach
    void setUp() {
        id = UUID.randomUUID();
        amount = new BigDecimal("100000");
        status = PaymentStatus.FULL;
        method = "Credit Card";
        salesTransactionId = 12345;
        createdAt = LocalDateTime.now();
    }

    @Test
    void testPaymentConstruction() {
        Payment payment = new Payment(id, amount, status, method, salesTransactionId, createdAt);

        assertEquals(id, payment.getId());
        assertEquals(amount, payment.getAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(method, payment.getMethod());
        assertEquals(salesTransactionId, payment.getSalesTransactionId());
        assertEquals(createdAt, payment.getCreatedAt());
    }

    @Test
    void testPaymentBuilder() {
        Payment payment = Payment.builder()
                .id(id)
                .amount(amount)
                .status(status)
                .method(method)
                .salesTransactionId(salesTransactionId)
                .createdAt(createdAt)
                .build();

        assertEquals(id, payment.getId());
        assertEquals(amount, payment.getAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(method, payment.getMethod());
        assertEquals(salesTransactionId, payment.getSalesTransactionId());
        assertEquals(createdAt, payment.getCreatedAt());
    }

    @Test
    void testNoArgsConstructor() {
        Payment payment = new Payment();
        assertNotNull(payment);
    }

    @Test
    void testSetters() {
        Payment payment = new Payment();
        payment.setId(id);
        payment.setAmount(amount);
        payment.setStatus(status);
        payment.setMethod(method);
        payment.setSalesTransactionId(salesTransactionId);
        payment.setCreatedAt(createdAt);

        assertEquals(id, payment.getId());
        assertEquals(amount, payment.getAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(method, payment.getMethod());
        assertEquals(salesTransactionId, payment.getSalesTransactionId());
        assertEquals(createdAt, payment.getCreatedAt());
    }
}
