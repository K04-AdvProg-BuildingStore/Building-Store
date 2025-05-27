package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model;

import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Calendar;

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

    @Test
    void testPurchaseWithNegativeQuantity() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Return Item")
                .quantity(-1) // Representing a return
                .totalAmount(-50000.0)
                .purchaseDate(new Date())
                .build();

        assertEquals(-1, purchase.getQuantity());
        assertEquals(-50000.0, purchase.getTotalAmount());
    }

    @Test
    void testPurchaseWithVeryLargeQuantity() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Nails")
                .quantity(10000)
                .totalAmount(1000000.0)
                .purchaseDate(new Date())
                .build();

        assertEquals(10000, purchase.getQuantity());
        assertEquals(1000000.0, purchase.getTotalAmount());
    }

    @Test
    void testPurchaseWithPastDate() {
        Calendar cal = Calendar.getInstance();
        cal.set(2020, Calendar.JANUARY, 1);
        Date pastDate = cal.getTime();

        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Historical Purchase")
                .quantity(1)
                .totalAmount(100000.0)
                .purchaseDate(pastDate)
                .build();

        assertEquals(pastDate, purchase.getPurchaseDate());
    }

    @Test
    void testPurchaseWithLongItemName() {
        String longItemName = "Super Deluxe Premium High-Performance Building Material with Extended Warranty and Professional Installation Service Package";
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName(longItemName)
                .quantity(1)
                .totalAmount(9999999.0)
                .purchaseDate(new Date())
                .build();

        assertEquals(longItemName, purchase.getItemName());
    }

    @Test
    void testPurchaseWithSpecialCharactersInItemName() {
        String specialItemName = "Item #123 (20% off!)";
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName(specialItemName)
                .quantity(1)
                .totalAmount(80000.0)
                .purchaseDate(new Date())
                .build();

        assertEquals(specialItemName, purchase.getItemName());
    }

    @Test
    void testPurchaseWithVeryPreciseTotalAmount() {
        double preciseAmount = 123456.789;
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Precision Item")
                .quantity(1)
                .totalAmount(preciseAmount)
                .purchaseDate(new Date())
                .build();

        assertEquals(preciseAmount, purchase.getTotalAmount());
    }

    @Test
    void testHashCodeMethod() {
        Date purchaseDate = new Date();
        PurchaseHistoryModel purchase1 = PurchaseHistoryModel.builder()
                .id(1)
                .phoneNumber("08123456789")
                .itemName("Test Item")
                .quantity(2)
                .totalAmount(100000.0)
                .purchaseDate(purchaseDate)
                .build();

        PurchaseHistoryModel purchase2 = PurchaseHistoryModel.builder()
                .id(1)
                .phoneNumber("08123456789")
                .itemName("Test Item")
                .quantity(2)
                .totalAmount(100000.0)
                .purchaseDate(purchaseDate)
                .build();

        assertEquals(purchase1.hashCode(), purchase2.hashCode());
    }

    @Test
    void testEqualsMethod() {
        Date purchaseDate = new Date();
        PurchaseHistoryModel purchase1 = PurchaseHistoryModel.builder()
                .id(1)
                .phoneNumber("08123456789")
                .itemName("Test Item")
                .quantity(2)
                .totalAmount(100000.0)
                .purchaseDate(purchaseDate)
                .build();

        PurchaseHistoryModel purchase2 = PurchaseHistoryModel.builder()
                .id(1)
                .phoneNumber("08123456789")
                .itemName("Test Item")
                .quantity(2)
                .totalAmount(100000.0)
                .purchaseDate(purchaseDate)
                .build();

        PurchaseHistoryModel differentPurchase = PurchaseHistoryModel.builder()
                .id(2)
                .phoneNumber("08123456789")
                .itemName("Different Item")
                .quantity(1)
                .totalAmount(50000.0)
                .purchaseDate(purchaseDate)
                .build();

        assertTrue(purchase1.equals(purchase2));
        assertTrue(purchase2.equals(purchase1));
        assertFalse(purchase1.equals(differentPurchase));
        assertFalse(purchase1.equals(null));
        assertFalse(purchase1.equals(new Object()));
    }

    @Test
    void testCanEqualMethod() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .build();

        // Ensure that canEqual works correctly
        assertTrue(purchase.canEqual(new PurchaseHistoryModel()));
        assertFalse(purchase.canEqual(new Object()));
        assertFalse(purchase.canEqual(null));
    }

    @Test
    void testToStringMethod() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .id(1)
                .phoneNumber("08123456789")
                .itemName("Test Item")
                .quantity(2)
                .totalAmount(100000.0)
                .purchaseDate(new Date())
                .build();

        String toString = purchase.toString();
        assertNotNull(toString);

        // Verify that the toString contains key fields
        assertTrue(toString.contains("phoneNumber"));
        assertTrue(toString.contains("08123456789"));
        assertTrue(toString.contains("itemName"));
        assertTrue(toString.contains("Test Item"));
        assertTrue(toString.contains("quantity"));
        assertTrue(toString.contains("totalAmount"));
    }

    @Test
    void testSetterMethods() {
        PurchaseHistoryModel purchase = new PurchaseHistoryModel();
        Date date = new Date();

        purchase.setId(1);
        purchase.setPhoneNumber("08123456789");
        purchase.setItemName("Test Item");
        purchase.setQuantity(2);
        purchase.setTotalAmount(100000.0);
        purchase.setPurchaseDate(date);

        assertEquals(Integer.valueOf(1), purchase.getId());
        assertEquals("08123456789", purchase.getPhoneNumber());
        assertEquals("Test Item", purchase.getItemName());
        assertEquals(2, purchase.getQuantity());
        assertEquals(100000.0, purchase.getTotalAmount());
        assertEquals(date, purchase.getPurchaseDate());
    }
}
