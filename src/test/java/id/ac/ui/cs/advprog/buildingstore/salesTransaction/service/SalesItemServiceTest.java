package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesItemRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.*;

public class SalesItemServiceTest {

    private SalesItemRepository repository;
    private SalesItemService service;

    @BeforeEach
    void setUp() {
        repository = Mockito.mock(SalesItemRepository.class);
        service = new SalesItemService(repository);
    }

    @Test
    void testCreateSalesItem() {
        ProductManagementModel product = ProductManagementModel.builder().id(1).build();
        SalesTransaction transaction = SalesTransaction.builder().id(1).build();
        SalesItem item = SalesItem.builder()
                .product(product)
                .transaction(transaction)
                .quantity(3)
                .price(50000)
                .build();

        when(repository.save(any(SalesItem.class))).thenReturn(item);

        SalesItem result = service.createSalesItem(product, transaction, 3, 50000);

        assertThat(result.getQuantity()).isEqualTo(3);
        assertThat(result.getPrice()).isEqualTo(50000);
    }

    @Test
    void testFindById_NotFound() {
        when(repository.findById("non-existent-id")).thenReturn(Optional.empty());
        Optional<SalesItem> result = service.findById("non-existent-id");
        assertThat(result).isEmpty();
    }
}
