package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.*;

class CustomerManagementModelTest {

    private CustomerManagementModel customer;

    @BeforeEach
    void setUp() {
        customer = CustomerManagementModel.builder()
                .name("John Doe")
                .phoneNumber("08123456789")
                .email("john.doe@example.com")
                .gender("Male")
                .birthday(new Date())
                .isActive(false)
                .build();
    }

    @Test
    void testCustomerInitialization() {
        assertEquals("John Doe", customer.getName());
        assertEquals("08123456789", customer.getPhoneNumber());
        assertEquals("john.doe@example.com", customer.getEmail());
        assertEquals("Male", customer.getGender());
        assertFalse(customer.isActive());
        assertNotNull(customer.getBirthday());
    }

    @Test
    void testActivateCustomer() {
        customer.activateCustomer();
        assertTrue(customer.isActive());
    }

    @Test
    void testDeactivateCustomer() {
        customer.setActive(true);
        customer.deactivateCustomer();
        assertFalse(customer.isActive());
    }

    @Test
    void testCustomerOnlyPhoneNumber() {
        CustomerManagementModel customer2 = CustomerManagementModel.builder()
                .phoneNumber("08123456789")
                .isActive(true)
                .build();
            

        assertEquals("08123456789", customer2.getPhoneNumber());
        assertNull(customer2.getName());
        assertNull(customer2.getEmail());
        assertNull(customer2.getGender());
        assertNull(customer2.getBirthday());
        assertTrue(customer2.isActive());
    }
    @Test
    void testCustomerBirthdayCanBeNull() {
    CustomerManagementModel customer = CustomerManagementModel.builder()
            .phoneNumber("0800000000")
            .birthday(null)
            .build();

    assertNull(customer.getBirthday());
    }
    @Test
    void testCustomerwithMinimalData() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .phoneNumber("0811223344")
                .build();

        assertEquals("0811223344", customer.getPhoneNumber());
        assertFalse(customer.isActive());
        assertNull(customer.getName());
        assertNull(customer.getEmail());
        assertNull(customer.getGender());
        assertNull(customer.getBirthday());
    }
}

