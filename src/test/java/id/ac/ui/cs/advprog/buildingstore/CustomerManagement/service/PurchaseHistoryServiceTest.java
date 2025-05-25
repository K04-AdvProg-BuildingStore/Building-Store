package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Arrays;

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
    void testGetCustomerPurchaseHistoryByIdReturnsMappedDTOs() {
        // Mocking the repository response for customer ID 1
        Object[] row1 = {1, "Alice", "0811111111", 101, 201, 2, 10000.0};
        Object[] row2 = {1, "Alice", "0811111111", 102, 202, 1, 5000.0};
        
        when(repository.findFullCustomerPurchaseHistoryByIdRaw(1))
                .thenReturn(Arrays.asList(row1, row2));

        List<PurchaseHistoryViewDTO> result = service.getCustomerPurchaseHistoryById(1);

        assertEquals(2, result.size());
        assertEquals(Integer.valueOf(1), result.get(0).getCustomerId());
        assertEquals("Alice", result.get(0).getCustomerName());
        assertEquals("0811111111", result.get(0).getPhoneNumber());
        assertEquals(Integer.valueOf(101), result.get(0).getTransactionId());
        assertEquals(Integer.valueOf(201), result.get(0).getProductId());
        assertEquals(Integer.valueOf(2), result.get(0).getQuantity());
        assertEquals(Double.valueOf(10000.0), result.get(0).getPrice());
        
        assertEquals(Integer.valueOf(102), result.get(1).getTransactionId());
        assertEquals(Integer.valueOf(202), result.get(1).getProductId());
        assertEquals(Integer.valueOf(1), result.get(1).getQuantity());
        assertEquals(Double.valueOf(5000.0), result.get(1).getPrice());
    }

    @Test
    void testGetCustomerPurchaseHistoryByIdReturnsEmptyListIfNoData() {
        when(repository.findFullCustomerPurchaseHistoryByIdRaw(999))
                .thenReturn(List.of());

        List<PurchaseHistoryViewDTO> result = service.getCustomerPurchaseHistoryById(999);

        assertTrue(result.isEmpty());
    }

    
    
    @Test
    void testAddPurchaseWithValidData() {
        // Create a purchase history model with valid data
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("0811111111")
                .itemName("Test Item")
                .quantity(2)
                .totalAmount(20000.0)
                .build();
        
        when(repository.save(purchase)).thenReturn(purchase);
        
        PurchaseHistoryModel result = service.addPurchase(purchase);
        
        assertNotNull(result);
        assertEquals("Test Item", result.getItemName());
        verify(repository).save(purchase);
    }
    
    @Test
    void testAddPurchaseWithInvalidPhoneNumber() {
        // Create a purchase history model with blank phone number
        PurchaseHistoryModel purchase = PurchaseHistoryModel.builder()
                .phoneNumber("")
                .itemName("Test Item")
                .quantity(1)
                .totalAmount(10000.0)
                .build();
        
        PurchaseHistoryModel result = service.addPurchase(purchase);
        
        assertNull(result);
        verify(repository, never()).save(any());
    }
}