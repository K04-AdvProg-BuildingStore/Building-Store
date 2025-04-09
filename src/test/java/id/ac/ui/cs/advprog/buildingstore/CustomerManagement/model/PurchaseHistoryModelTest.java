package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model;

import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class PurchaseHistoryModelTest {

    @Test
    void testPurchaseHistoryCreation() {
        Date purchaseDate = new Date();

        PurchaseHistoryModel history = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Gaming Keyboard")
                .quantity(2)
                .totalAmount(700000.0)
                .purchaseDate(purchaseDate)
                .build();

        assertEquals("08123456789", history.getPhoneNumber());
        assertEquals("Gaming Keyboard", history.getItemName());
        assertEquals(2, history.getQuantity());
        assertEquals(700000.0, history.getTotalAmount());
        assertEquals(purchaseDate, history.getPurchaseDate());
    }

    @Test
void testPurchaseWithNullPhoneNumber() {
    PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
            .phoneNumber(null)
            .itemName("Cable")
            .quantity(1)
            .totalAmount(5000.0)
            .purchaseDate(new Date())
            .build();

    assertNull(purchase.getPhoneNumber());  // Should be allowed per business rule
}

@Test
void testPurchaseWithZeroQuantity() {
    PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
            .phoneNumber("0811223344")
            .itemName("Mouse")
            .quantity(0)
            .totalAmount(0.0)
            .purchaseDate(new Date())
            .build();

    assertEquals(0, purchase.getQuantity());
    assertEquals(0.0, purchase.getTotalAmount());
}

@Test
void testPurchaseCanHaveLargeAmount() {
    PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
            .phoneNumber("0811333444")
            .itemName("Smartphone")
            .quantity(5)
            .totalAmount(12500000.0)
            .purchaseDate(new Date())
            .build();

    assertEquals("Smartphone", purchase.getItemName());
    assertEquals(5, purchase.getQuantity());
    assertTrue(purchase.getTotalAmount() > 10000000.0);
}

}
