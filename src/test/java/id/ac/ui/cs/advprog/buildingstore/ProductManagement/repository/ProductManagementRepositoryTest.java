package id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class ProductManagementRepositoryTest {

    @Autowired
    private ProductManagementRepository repository;

    @Test
    void testSaveAndFindById() {
        ProductManagementModel product = ProductManagementModel.builder()
                .name("Pokemon Pack")
                .quantity(10)
                .price(50000)
                .status("Available")
                .information("Get CHARIZARD")
                .build();

        repository.save(product);

        Optional<ProductManagementModel> found = repository.findById(product.getId());

        assertTrue(found.isPresent());
        assertEquals("Pokemon Pack", found.get().getName());
        assertEquals(10, found.get().getQuantity());
        assertEquals("Available", found.get().getStatus());
    }

    @Test
    void testDeleteProduct() {
        ProductManagementModel product = ProductManagementModel.builder()
                .name("Macbook Pro")
                .quantity(5)
                .price(25000)
                .status("Available")
                .information("Very fast Laptop")
                .build();

        repository.save(product);

        Optional<ProductManagementModel> beforeDelete = repository.findById(product.getId());
        assertTrue(beforeDelete.isPresent());

        repository.deleteById(product.getId());

        Optional<ProductManagementModel> afterDelete = repository.findById(product.getId());
        assertFalse(afterDelete.isPresent());
    }
}
