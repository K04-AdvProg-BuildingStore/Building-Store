package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTOImpl;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

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
    void testGetCustomerPurchaseHistoryReturnsMappedDTOs() {
        Object[] row1 = {"Alice", "0811111111", 1, 1, 1, 2, 10000.0}; // status and productId as Integer
        Object[] row2 = {"Alice", "0811111111", 2, 0, 2, 1, 5000.0}; // status and productId as Integer
        when(repository.findFullCustomerPurchaseHistoryRaw("0811111111"))
                .thenReturn(List.of(row1, row2));

        List<PurchaseHistoryViewDTO> result = service.getCustomerPurchaseHistory("0811111111");

        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getCustomerName());
        assertEquals(1, result.get(0).getProductId());
        assertEquals(2, result.get(0).getQuantity());
        assertEquals(0, result.get(1).getStatus());
    }

    @Test
    void testGetCustomerPurchaseHistoryReturnsEmptyListIfNoData() {
        when(repository.findFullCustomerPurchaseHistoryRaw("0000000000"))
                .thenReturn(List.of());

        List<PurchaseHistoryViewDTO> result = service.getCustomerPurchaseHistory("0000000000");

        assertTrue(result.isEmpty());
    }
}