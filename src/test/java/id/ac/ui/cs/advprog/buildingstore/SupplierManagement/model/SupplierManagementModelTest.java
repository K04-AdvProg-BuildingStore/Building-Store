package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model;
import static org.junit.jupiter.api.Assertions.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

public class SupplierManagementModelTest {

    private SupplierManagementModel supplier;

    @BeforeEach
    public void setup() {
        // Initialize with default values
        supplier = new SupplierManagementModel();
        supplier.setId(1);
        supplier.setName("Supplier Test");
        supplier.setPhoneNumber("1234567890");
        supplier.setAddress("Test Address");
        supplier.setActive(false);
    }

    @Test
    public void testActivateSupplier() {
        // Ensure the supplier is inactive initially
        assertFalse(supplier.isActive(), "Supplier should initially be inactive.");
        supplier.activateSupplier();
        assertTrue(supplier.isActive(), "Supplier should be activated after calling activateSupplier().");
    }

    @Test
    public void testDeactivateSupplier() {
        // Set supplier active then deactivate it
        supplier.setActive(true);
        assertTrue(supplier.isActive(), "Supplier should initially be active.");
        supplier.deactivateSupplier();
        assertFalse(supplier.isActive(), "Supplier should be deactivated after calling deactivateSupplier().");
    }

    @Test
    public void testParameterizedConstructor() {
        // Test the all argument constructor for proper initialization
        SupplierManagementModel newSupplier = new SupplierManagementModel(
                2,
                "Test Supplier",
                "0987654321",
                "Another Address",
                false
        );

        assertEquals(2, newSupplier.getId(), "Supplier id should match the provided value.");
        assertEquals("Test Supplier", newSupplier.getName(), "Supplier name should match the provided value.");
        assertEquals("0987654321", newSupplier.getPhoneNumber(), "Supplier phone number should match the provided value.");
        assertEquals("Another Address", newSupplier.getAddress(), "Supplier address should match the provided value.");
        assertFalse(newSupplier.isActive(), "Supplier active status should match the provided value.");
    }

    @Test
    public void testSettersAndGetters() {
        // Set different values to test the setters and getters
        supplier.setId(10);
        supplier.setName("New Supplier Name");
        supplier.setPhoneNumber("5555555555");
        supplier.setAddress("123 Main Street, Anytown");
        supplier.setActive(true);

        assertEquals(10, supplier.getId(), "The id should be set to 10.");
        assertEquals("New Supplier Name", supplier.getName(), "The name should be updated.");
        assertEquals("5555555555", supplier.getPhoneNumber(), "The phone number should be updated.");
        assertEquals("123 Main Street, Anytown", supplier.getAddress(), "The address should be updated.");
        assertTrue(supplier.isActive(), "The active flag should be true.");
    }

    @Test
    public void testEqualityAndHashcode() {
        // Create two objects with the same data
        SupplierManagementModel supplier1 = new SupplierManagementModel(
                3, "Equality Test", "1112223333", "Equality Address", true
        );
        SupplierManagementModel supplier2 = new SupplierManagementModel(
                3, "Equality Test", "1112223333", "Equality Address", true
        );

        assertEquals(supplier1, supplier2, "Both suppliers should be considered equal.");
        assertEquals(supplier1.hashCode(), supplier2.hashCode(), "Hashcodes should match for equal objects.");
    }

    @Test
    public void testInequality() {
        // Create two objects with different values
        SupplierManagementModel supplier1 = new SupplierManagementModel(
                4, "Supplier One", "4445556666", "Address One", true
        );
        SupplierManagementModel supplier2 = new SupplierManagementModel(
                5, "Supplier Two", "7778889999", "Address Two", false
        );
        assertNotEquals(supplier1, supplier2, "Suppliers with different data should not be equal.");
    }

    @Test
    public void testToString() {
        // The toString method should return a non null
        String string = supplier.toString();
        assertNotNull(string, "toString() should not return null.");
        assertFalse(string.isEmpty(), "toString() should not return an empty string.");
    }
}
