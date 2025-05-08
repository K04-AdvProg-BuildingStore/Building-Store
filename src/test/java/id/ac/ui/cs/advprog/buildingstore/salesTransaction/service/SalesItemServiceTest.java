package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesItemRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.*;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesItemServiceTest {

    @Mock
    private SalesItemRepository salesItemRepository;

    @Mock
    private SalesTransactionRepository salesTransactionRepository;

    @InjectMocks
    private SalesItemService salesItemService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateSalesItem() {
        SalesTransaction tx = SalesTransaction.builder().id(1).build();
        SalesItem item = SalesItem.builder().transaction(tx).quantity(5).price(10000).build();

        when(salesTransactionRepository.findById(1)).thenReturn(Optional.of(tx));
        when(salesItemRepository.save(any(SalesItem.class))).thenReturn(item);

        SalesItem created = salesItemService.createSalesItem(tx, 5, 10000);

        assertNotNull(created);
        assertEquals(5, created.getQuantity());
        assertEquals(10000, created.getPrice());
        verify(salesItemRepository).save(any(SalesItem.class));
    }

    @Test
    void testCreateSalesItemTransactionNotFound() {
        SalesTransaction tx = SalesTransaction.builder().id(999).build();
        when(salesTransactionRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> salesItemService.createSalesItem(tx, 1, 5000));
    }

    @Test
    void testGetSalesItemByIdFound() {
        SalesItem item = SalesItem.builder().id(Integer.parseInt("321")).build(); // <-- FIXED
        when(salesItemRepository.findById("321")).thenReturn(Optional.of(item));

        Optional<SalesItem> result = salesItemService.getSalesItemById("321");

        assertTrue(result.isPresent());
        assertEquals(321, result.get().getId()); // now both sides are strings
    }

    @Test
    void testUpdateSalesItemSuccessfully() {
        SalesTransaction tx = SalesTransaction.builder().id(1).build();
        SalesItem item = SalesItem.builder().id(Integer.parseInt("123")).transaction(tx).quantity(2).price(100).build();

        when(salesItemRepository.findById("123")).thenReturn(Optional.of(item));
        when(salesTransactionRepository.findById(1)).thenReturn(Optional.of(tx));
        when(salesItemRepository.save(any(SalesItem.class))).thenReturn(item);

        SalesItem updated = salesItemService.updateSalesItem(123, tx, 3, 200);

        assertEquals(3, updated.getQuantity());
        assertEquals(200, updated.getPrice());
    }

    @Test
    void testDeleteSalesItemSuccessfully() {
        SalesItem item = SalesItem.builder().id(Integer.parseInt("321")).build();
        when(salesItemRepository.findById("321")).thenReturn(Optional.of(item));

        salesItemService.deleteSalesItem(321);

        verify(salesItemRepository).delete(item);
    }

    @Test
    void testDeleteSalesItemNotFound() {
        when(salesItemRepository.findById("404")).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> salesItemService.deleteSalesItem(404));
    }
}
