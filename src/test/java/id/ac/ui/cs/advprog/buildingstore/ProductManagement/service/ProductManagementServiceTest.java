package id.ac.ui.cs.advprog.buildingstore.ProductManagement.service;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class ProductManagementServiceTest {

    private ProductManagementService service;
    private ProductManagementRepository repository;

    @BeforeEach
    void setUp() {
        repository = mock(ProductManagementRepository.class);
        service = new ProductManagementServiceImpl(repository);
    }

    @Test
    void testAddProductSuccess() {
        ProductManagementModel product = ProductManagementModel.builder()
                .id(1)
                .name("Concrete")
                .price(50000)
                .quantity(10)
                .status("Available")
                .information("Strong and reliable")
                .build();

        when(repository.save(product)).thenReturn(product);

        ProductManagementModel result = service.addProduct(product);

        assertNotNull(result);
        assertEquals("Concrete", result.getName());
        verify(repository, times(1)).save(product);
    }

    @Test
    void testAddProductWithNullNameShouldNotBeSaved() {
        ProductManagementModel product = ProductManagementModel.builder()
                .name(null)
                .price(10000)
                .quantity(5)
                .build();

        ProductManagementModel result = service.addProduct(product);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testGetProductById() {
        ProductManagementModel product = ProductManagementModel.builder()
                .id(1)
                .name("Cement")
                .price(40000)
                .quantity(20)
                .status("Available")
                .build();

        when(repository.findById(1)).thenReturn(Optional.of(product));

        Optional<ProductManagementModel> result = service.getProductById(1);

        assertTrue(result.isPresent());
        assertEquals("Cement", result.get().getName());
    }

    @Test
    void testDeleteProductById() {
        service.deleteProductById(1);
        verify(repository, times(1)).deleteById(1);
    }

    @Test
    void testUpdateProductInfoSuccess() {
        Integer id = 1;

        ProductManagementModel existing = ProductManagementModel.builder()
                .id(id)
                .name("Old Product")
                .price(25000)
                .quantity(5)
                .status("Out of Stock")
                .information("Old info")
                .build();

        when(repository.findById(id)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        ProductManagementModel updated = service.updateProductInfo(
                id,
                "New Product",
                60000,
                15,
                "Available",
                "Updated info"
        );

        assertNotNull(updated);
        assertEquals("New Product", updated.getName());
        assertEquals(60000, updated.getPrice());
        assertEquals(15, updated.getQuantity());
        assertEquals("Available", updated.getStatus());
        assertEquals("Updated info", updated.getInformation());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateProductInfoProductNotFound() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        ProductManagementModel result = service.updateProductInfo(
                999, "Ghost Product", 0, 0, "Unavailable", "No info"
        );

        assertNull(result);
        verify(repository, never()).save(any());
    }
    @Test
    void testSearchProductsByName() {
        ProductManagementModel hammer = ProductManagementModel.builder()
                .id(1)
                .name("Hammer")
                .quantity(10)
                .price(50000)
                .status("Available")
                .information("Steel hammer")
                .build();

        when(repository.findByNameContainingIgnoreCase("Ham"))
                .thenReturn(List.of(hammer));

        List<ProductManagementModel> results = service.searchProductsByName("Ham");

        assertEquals(1, results.size());
        assertEquals("Hammer", results.get(0).getName());
        verify(repository, times(1)).findByNameContainingIgnoreCase("Ham");
    }
    @Test
    void testSetProductStock_updatesQuantityAndStatus() {
        ProductManagementModel product = new ProductManagementModel();
        product.setId(8);
        product.setQuantity(10);
        product.setStatus("Available");

        when(repository.findById(8)).thenReturn(Optional.of(product));

        service.setProductStock(8, 0);

        assertEquals(0, product.getQuantity());
        assertEquals("Out of Stock", product.getStatus());
        verify(repository).save(product);
    }

}
