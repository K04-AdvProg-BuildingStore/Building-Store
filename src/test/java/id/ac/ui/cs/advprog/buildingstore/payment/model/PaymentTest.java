package id.ac.ui.cs.advprog.buildingstore.payment.model;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.Test;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;

class PaymentTest {

    @Test
    void testPaymentBuilder() {
        UUID id = UUID.randomUUID();
        int amount = 100000;
        PaymentStatus status = PaymentStatus.PAID;
        String method = "Credit Card";
        String salesTransactionId = "ST12345";

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
        assertEquals(0, payment.getAmount());
        assertNull(payment.getStatus());
        assertNull(payment.getMethod());
        assertNull(payment.getSalesTransactionId());
    }

    @Test
    void testPaymentAllArgsConstructor() {
        UUID id = UUID.randomUUID();
        int amount = 100000;
        PaymentStatus status = PaymentStatus.INSTALLMENT;
        String method = "Debit Card";
        String salesTransactionId = "ST67890";

        Payment payment = new Payment(id, amount, status, method, salesTransactionId);

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
        
        payment.setAmount(200000);
        payment.setStatus(PaymentStatus.PAID);
        payment.setMethod("Bank Transfer");
        payment.setSalesTransactionId("ST24680");
        
        assertEquals(id, payment.getId());
        assertEquals(200000, payment.getAmount());
        assertEquals(PaymentStatus.PAID, payment.getStatus());
        assertEquals("Bank Transfer", payment.getMethod());
        assertEquals("ST24680", payment.getSalesTransactionId());
    }
}
