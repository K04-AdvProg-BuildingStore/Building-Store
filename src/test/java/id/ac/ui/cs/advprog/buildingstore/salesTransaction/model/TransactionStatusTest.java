package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TransactionStatusTest {

    @Test
    void testGetValue() {
        assertEquals(0, TransactionStatus.PENDING.getValue());
        assertEquals(1, TransactionStatus.PAID.getValue());
    }

    @Test
    void testFromValueReturnsCorrectStatus() {
        assertEquals(TransactionStatus.PENDING, TransactionStatus.fromValue(0));
        assertEquals(TransactionStatus.PAID, TransactionStatus.fromValue(1));
    }

    @Test
    void testFromValueThrowsOnInvalidValue() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            TransactionStatus.fromValue(99);
        });
        assertTrue(exception.getMessage().contains("Invalid transaction status value"));
    }
}