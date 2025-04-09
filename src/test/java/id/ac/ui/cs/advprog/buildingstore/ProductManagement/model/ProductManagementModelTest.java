package id.ac.ui.cs.advprog.buildingstore.ProductManagement.model;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProductManagementModelTest{
    private ProductManagementModel product;

    @BeforeEach
    void setUp() {
        User adminUser = User.builder()
                .id(1)
                .username("admin123")
                .password("password")
                .role(Role.ADMIN)
                .build();

        product = ProductManagementModel.builder()
                .name("Soda")
                .quantity(10)
                .price(1000)
                .administrator(adminUser)
                .status("Available")
                .information("Amazing Incredible Yummy Soda")
                .build();

    }
    @Test
    void testProductInitialization(){
        assertEquals("Soda", product.getName());
        assertEquals(10, product.getQuantity());
        assertEquals(1000, product.getPrice());

        assertNotNull(product.getAdministrator());
        assertEquals(1, product.getAdministrator().getId());
        assertEquals("admin123", product.getAdministrator().getUsername());
        assertEquals(Role.ADMIN, product.getAdministrator().getRole());

        assertEquals("Available", product.getStatus());
        assertEquals("Amazing Incredible Yummy Soda", product.getInformation());
    }

    @Test
    void testUpdateStock() {
        product.setQuantity(25);
        assertEquals(25, product.getQuantity());
    }

    @Test
    void testChangeStatus() {
        product.setStatus("Discontinued");
        assertEquals("Discontinued", product.getStatus());
    }
}