package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

public class SupplyModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        SupplyModel supply = new SupplyModel();
        assertNull(supply.getId());
        assertNull(supply.getSupplier());
        assertNull(supply.getSupplyStock());
        assertNull(supply.getDeliveryAddress());

        SupplierManagementModel supplier = new SupplierManagementModel();
        supplier.setId(1);
        supplier.setName("Test Supplier");
        supplier.setPhoneNumber("12345");
        supplier.setAddress("Test Address");
        supplier.setActive(true);

        supply.setId(10);
        supply.setSupplier(supplier);
        supply.setSupplyStock(50);
        supply.setDeliveryAddress("Warehouse A");

        assertEquals(10, supply.getId());
        assertEquals(supplier, supply.getSupplier());
        assertEquals(50, supply.getSupplyStock());
        assertEquals("Warehouse A", supply.getDeliveryAddress());
    }

    @Test
    void testAllArgsConstructor() {
        SupplierManagementModel supplier = new SupplierManagementModel(2, "Supplier2", "67890", "Addr2", false);
        SupplyModel supply = new SupplyModel(20, supplier, 75, "Warehouse B");

        assertEquals(20, supply.getId());
        assertEquals(supplier, supply.getSupplier());
        assertEquals(75, supply.getSupplyStock());
        assertEquals("Warehouse B", supply.getDeliveryAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        SupplierManagementModel supplier1 = new SupplierManagementModel(3, "Supplier3", "11111", "Addr3", true);
        SupplierManagementModel supplier2 = new SupplierManagementModel(3, "Supplier3", "11111", "Addr3", true);
        SupplyModel s1 = new SupplyModel(30, supplier1, 100, "Warehouse C");
        SupplyModel s2 = new SupplyModel(30, supplier2, 100, "Warehouse C");

        assertEquals(s1, s2);
        assertEquals(s1.hashCode(), s2.hashCode());
    }

    @Test
    void testToStringContainsAllFields() {
        SupplierManagementModel supplier = new SupplierManagementModel(4, "Supplier4", "22222", "Addr4", true);
        SupplyModel supply = new SupplyModel(40, supplier, 150, "Warehouse D");
        String str = supply.toString();
        assertTrue(str.contains("id=40"));
        assertTrue(str.contains("supplier="));
        assertTrue(str.contains("supplyStock=150"));
        assertTrue(str.contains("deliveryAddress=Warehouse D"));
    }
}
