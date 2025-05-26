package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class PurchaseHistoryRepositoryTest {

    @Mock
    private PurchaseHistoryRepository repository;

    @Test
    void testFindFullCustomerPurchaseHistoryRawReturnsCorrectData() {
        // Given
        Integer customerId = 1;
        Object[] mockRow = new Object[] {
            1,                  // customer_id
            "Alice",            // customer_name
            "0811111111",       // phone_number
            2,                  // transaction_id
            3,                  // product_id
            2,                  // quantity
            10000               // price
        };
        List<Object[]> mockResult = new ArrayList<>();
        mockResult.add(mockRow);
        
        // When
        when(repository.findFullCustomerPurchaseHistoryByIdRaw(customerId))
            .thenReturn(mockResult);
        
        // Then
        List<Object[]> result = repository.findFullCustomerPurchaseHistoryByIdRaw(customerId);
        
        assertEquals(1, result.size());
        Object[] row = result.get(0);
        assertEquals(1, ((Number) row[0]).intValue());
        assertEquals("Alice", row[1]);
        assertEquals("0811111111", row[2]);
        assertEquals(2, ((Number) row[3]).intValue());
        assertEquals(3, ((Number) row[4]).intValue());
        assertEquals(2, ((Number) row[5]).intValue());
        assertEquals(10000, ((Number) row[6]).intValue());
        
        verify(repository, times(1)).findFullCustomerPurchaseHistoryByIdRaw(customerId);
    }
}