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
}
