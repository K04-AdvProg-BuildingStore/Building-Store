package id.ac.ui.cs.advprog.buildingstore.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;

class PaymentTest {

    @Test
    void testPaymentBuilder() {
        UUID id = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100000);
        PaymentStatus status = PaymentStatus.FULL;
        String method = "Credit Card";
        Integer salesTransactionId = 1738;

        Payment payment = Payment.builder()
                .id(id)
                .amount(amount)
                .status(status)
                .method(method)
                .salesTransactionId(salesTransactionId)
                .build();

        assertEquals(id, payment.getId());
        assertEquals(amount, payment.getAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(method, payment.getMethod());
        assertEquals(salesTransactionId, payment.getSalesTransactionId());
    }

    @Test
    void testPaymentNoArgsConstructor() {
        Payment payment = new Payment();
        
        assertNull(payment.getId());
        assertNull(payment.getAmount());
        assertNull(payment.getStatus());
        assertNull(payment.getMethod());
        assertNull(payment.getSalesTransactionId());
    }

    @Test
    void testPaymentAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        BigDecimal amount = BigDecimal.valueOf(100000);
        PaymentStatus status = PaymentStatus.INSTALLMENT;
        String method = "Debit Card";
        Integer salesTransactionId = 67890;

        Payment payment = new Payment(id, amount, status, method, salesTransactionId, LocalDateTime.now());

        assertEquals(id, payment.getId());
        assertEquals(amount, payment.getAmount());
        assertEquals(status, payment.getStatus());
        assertEquals(method, payment.getMethod());
        assertEquals(salesTransactionId, payment.getSalesTransactionId());
    }

    @Test
    void testPaymentSettersAndGetters() {
        Payment payment = new Payment();
        
        UUID id = UUID.randomUUID();
        payment.setId(id);
        
        payment.setAmount(BigDecimal.valueOf(200000));
        payment.setStatus(PaymentStatus.FULL);
        payment.setMethod("Bank Transfer");
        payment.setSalesTransactionId(24680);
        
        assertEquals(id, payment.getId());
        assertEquals(BigDecimal.valueOf(200000), payment.getAmount());
        assertEquals(PaymentStatus.FULL, payment.getStatus());
        assertEquals("Bank Transfer", payment.getMethod());
        assertEquals(24680, payment.getSalesTransactionId());
    }
}
