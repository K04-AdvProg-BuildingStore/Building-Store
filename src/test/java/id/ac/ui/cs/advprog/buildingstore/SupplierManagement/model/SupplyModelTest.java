package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class SupplyModelTest {

    @Test
    void testNoArgsConstructorAndSetters() {
        SupplyModel model = new SupplyModel();

        model.setId(42);
        model.setSuppliedProduct("Widget");
        model.setPhoneNumber("08123456789");
        model.setSupplyStock(100);
        model.setAddress("Jl. Sudirman No.1");

        assertEquals(42, model.getId());
        assertEquals("Widget", model.getSuppliedProduct());
        assertEquals("08123456789", model.getPhoneNumber());
        assertEquals(100, model.getSupplyStock());
        assertEquals("Jl. Sudirman No.1", model.getAddress());
    }

    @Test
    void testAllArgsConstructorAndGetters() {
        SupplyModel model = new SupplyModel(
                7,
                "Gadget",
                "08987654321",
                50,
                "Jl. Thamrin No.2"
        );

        assertEquals(7, model.getId());
        assertEquals("Gadget", model.getSuppliedProduct());
        assertEquals("08987654321", model.getPhoneNumber());
        assertEquals(50, model.getSupplyStock());
        assertEquals("Jl. Thamrin No.2", model.getAddress());
    }

    @Test
    void testEqualsAndHashCode() {
        SupplyModel a = new SupplyModel(1, "Item", "0800000000", 10, "Addr A");
        SupplyModel b = new SupplyModel(1, "Item", "0800000000", 10, "Addr A");
        SupplyModel c = new SupplyModel(2, "Item", "0800000000", 10, "Addr A");

        assertEquals(a, b);
        assertEquals(a.hashCode(), b.hashCode());
        assertNotEquals(a, c);
    }
}
