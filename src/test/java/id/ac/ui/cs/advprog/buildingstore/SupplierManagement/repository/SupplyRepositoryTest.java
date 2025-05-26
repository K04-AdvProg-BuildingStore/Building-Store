package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@AutoConfigureTestDatabase(replace = Replace.NONE)
@Rollback
public class SupplyRepositoryTest {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private SupplierManagementRepository supplierRepository;

    @Autowired
    private ProductManagementRepository productRepository;

    @BeforeEach
    public void cleanDatabase() {
        // Only clear supply and supplier tables; avoid product deletion due to external FK constraints
        supplyRepository.deleteAll();
        supplierRepository.deleteAll();
    }

    /**
     * Helper to create and save a SupplierManagementModel
     */
    private SupplierManagementModel createSupplier() {
        SupplierManagementModel supplier = new SupplierManagementModel();
        supplier.setName("Test Supplier");
        String phone = "0812345" + System.nanoTime();
        supplier.setPhoneNumber(phone);
        supplier.setAddress("Test Address");
        supplier.setActive(true);
        return supplierRepository.save(supplier);
    }

    /**
     * Helper to create and save a ProductManagementModel
     */
    private ProductManagementModel createProduct() {
        ProductManagementModel product = ProductManagementModel.builder()
                .name("Test Product")
                .quantity(100)
                .price(500)
                .status("Available")
                .information("Test Info")
                .administrator(null)
                .build();
        return productRepository.save(product);
    }

    @Test
    public void testFindBySupplierId_andFindByProductId() {
        // Given: a saved supplier and product
        SupplierManagementModel savedSupplier = createSupplier();
        ProductManagementModel savedProduct = createProduct();

        // And: a supply record linking them
        SupplyModel supply = new SupplyModel();
        supply.setSupplier(savedSupplier);
        supply.setProduct(savedProduct);
        supply.setSupplyStock(50);
        supply.setDeliveryAddress("Warehouse A");
        SupplyModel savedSupply = supplyRepository.save(supply);

        // When: searching by supplierId
        List<SupplyModel> bySupplier = supplyRepository.findBySupplierId(savedSupplier.getId());
        assertNotNull(bySupplier);
        assertEquals(1, bySupplier.size());
        assertEquals(savedSupply.getId(), bySupplier.get(0).getId());

        // When: searching by productId
        List<SupplyModel> byProduct = supplyRepository.findByProductId(savedProduct.getId());
        assertNotNull(byProduct);
        assertEquals(1, byProduct.size());
        assertEquals(savedSupply.getId(), byProduct.get(0).getId());
    }

    @Test
    public void testSaveAndDeleteSupply() {
        // Given: a supplier and product
        SupplierManagementModel supplier = createSupplier();
        ProductManagementModel product = createProduct();

        // And: a new supply
        SupplyModel supply = new SupplyModel();
        supply.setSupplier(supplier);
        supply.setProduct(product);
        supply.setSupplyStock(20);
        supply.setDeliveryAddress("Warehouse B");
        SupplyModel saved = supplyRepository.save(supply);

        // Ensure it was saved
        Optional<SupplyModel> found = supplyRepository.findById(saved.getId());
        assertTrue(found.isPresent());

        // When: deleting the supply
        supplyRepository.delete(saved);

        // Then: it should no longer exist
        Optional<SupplyModel> afterDelete = supplyRepository.findById(saved.getId());
        assertFalse(afterDelete.isPresent());
    }
}