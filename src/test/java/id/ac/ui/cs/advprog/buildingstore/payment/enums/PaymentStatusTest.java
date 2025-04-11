package id.ac.ui.cs.advprog.buildingstore.payment.enums;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class PaymentStatusTest {

    @Test
    void testPaymentStatusValues() {
        PaymentStatus[] statuses = PaymentStatus.values();
        
        assertEquals(2, statuses.length);
        assertEquals(PaymentStatus.PAID, statuses[0]);
        assertEquals(PaymentStatus.INSTALLMENT, statuses[1]);
    }

    @Test
    void testPaymentStatusValueOf_WithValidValues() {
        assertEquals(PaymentStatus.PAID, PaymentStatus.valueOf("PAID"));
        assertEquals(PaymentStatus.INSTALLMENT, PaymentStatus.valueOf("INSTALLMENT"));
    }
    
    @Test
    void testPaymentStatusValueOf_WithInvalidValue_ShouldThrowException() {
        assertThrows(IllegalArgumentException.class, () -> {
            PaymentStatus.valueOf("INVALID_STATUS");
        });
    }
    
    @Test
    void testPaymentStatusToString() {
        assertEquals("PAID", PaymentStatus.PAID.toString());
        assertEquals("INSTALLMENT", PaymentStatus.INSTALLMENT.toString());
    }
    
    @Test
    void testPaymentStatusOrdinals() {
        assertEquals(0, PaymentStatus.PAID.ordinal());
        assertEquals(1, PaymentStatus.INSTALLMENT.ordinal());
    }
}
