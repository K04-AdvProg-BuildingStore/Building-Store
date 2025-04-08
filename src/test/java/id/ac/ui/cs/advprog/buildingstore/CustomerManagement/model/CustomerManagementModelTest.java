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
}
