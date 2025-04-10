package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.List;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

class PurchaseHistoryServiceTest {

    private PurchaseHistoryRepository repository;
    private PurchaseHistoryService service;

    @BeforeEach
    void setUp() {
        repository = mock(PurchaseHistoryRepository.class);
        service = new PurchaseHistoryService(repository);
    }

    @Test
    void testAddPurchaseWithPhoneNumber() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Keyboard")
                .quantity(1)
                .totalAmount(350000.0)
                .purchaseDate(new Date())
                .build();

        when(repository.save(purchase)).thenReturn(purchase);

        PurchaseHistoryModel result = service.addPurchase(purchase);
        assertNotNull(result);
        assertEquals("Keyboard", result.getItemName());
        verify(repository, times(1)).save(purchase);
    }

    @Test
    void testAddPurchaseWithNullPhoneNumberShouldNotBeSaved() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber(null)
                .itemName("Mouse")
                .quantity(1)
                .totalAmount(150000.0)
                .purchaseDate(new Date())
                .build();

        PurchaseHistoryModel result = service.addPurchase(purchase);
        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testAddPurchaseWithBlankPhoneNumberShouldNotBeSaved() {
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber(" ")
                .itemName("Cable")
                .quantity(2)
                .totalAmount(50000.0)
                .purchaseDate(new Date())
                .build();

        PurchaseHistoryModel result = service.addPurchase(purchase);
        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testGetPurchaseHistoryByPhoneNumber() {
        List<PurchaseHistoryModel> purchases = List.of(
                PurchaseHistoryModel.builder().phoneNumber("0811223344").itemName("Monitor").quantity(1).build(),
                PurchaseHistoryModel.builder().phoneNumber("0811223344").itemName("HDMI Cable").quantity(2).build()
        );

        when(repository.findByPhoneNumber("0811223344")).thenReturn(purchases);

        List<PurchaseHistoryModel> result = service.getPurchaseHistory("0811223344");

        assertEquals(2, result.size());
        assertEquals("Monitor", result.get(0).getItemName());
    }

    @Test
    void testGetPurchaseHistoryForUnknownPhoneReturnsEmpty() {
        when(repository.findByPhoneNumber("0000000000")).thenReturn(List.of());

        List<PurchaseHistoryModel> result = service.getPurchaseHistory("0000000000");

        assertTrue(result.isEmpty());
    }
}
