package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Date;
import java.util.Calendar;

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

    @Test
    void testCustomerWithSpecialCharactersInName() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("John O'Brien-Smith Jr.")
                .phoneNumber("08111222333")
                .build();

        assertEquals("John O'Brien-Smith Jr.", customer.getName());
        assertEquals("08111222333", customer.getPhoneNumber());
    }

    @Test
    void testCustomerWithEmptyName() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("")
                .phoneNumber("08111222444")
                .build();

        assertEquals("", customer.getName());
    }

    @Test
    void testCustomerWithVeryLongName() {
        String longName = "A".repeat(100); // 100 character name
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name(longName)
                .phoneNumber("08111222555")
                .build();

        assertEquals(longName, customer.getName());
        assertEquals(100, customer.getName().length());
    }

    @Test
    void testCustomerWithInternationalPhoneNumber() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("International Customer")
                .phoneNumber("+62811222666")
                .build();

        assertEquals("+62811222666", customer.getPhoneNumber());
    }

    @Test
    void testCustomerWithPastBirthday() {
        Calendar cal = Calendar.getInstance();
        cal.set(1990, Calendar.JANUARY, 1);
        Date pastDate = cal.getTime();
        
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("Past Birthday Customer")
                .phoneNumber("08111222777")
                .birthday(pastDate)
                .build();

        assertEquals(pastDate, customer.getBirthday());
    }

    @Test
    void testCustomerStatusToggling() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .phoneNumber("08111222888")
                .isActive(false)
                .build();
        
        assertFalse(customer.isActive());
        
        customer.activateCustomer();
        assertTrue(customer.isActive());
        
        customer.deactivateCustomer();
        assertFalse(customer.isActive());
        
        customer.activateCustomer();
        assertTrue(customer.isActive());
    }
    
    @Test
    void testHashCodeMethod() {
        Date birthday = new Date();
        CustomerManagementModel customer1 = CustomerManagementModel.builder()
                .id(1)
                .name("Test Customer")
                .phoneNumber("08123456789")
                .email("test@example.com")
                .gender("Male")
                .birthday(birthday)
                .isActive(true)
                .build();
        
        CustomerManagementModel customer2 = CustomerManagementModel.builder()
                .id(1)
                .name("Test Customer")
                .phoneNumber("08123456789")
                .email("test@example.com")
                .gender("Male")
                .birthday(birthday)
                .isActive(true)
                .build();
        
        assertEquals(customer1.hashCode(), customer2.hashCode());
    }
    
    @Test
    void testEqualsMethod() {
        Date birthday = new Date();
        CustomerManagementModel customer1 = CustomerManagementModel.builder()
                .id(1)
                .name("Test Customer")
                .phoneNumber("08123456789")
                .email("test@example.com")
                .gender("Male")
                .birthday(birthday)
                .isActive(true)
                .build();
        
        CustomerManagementModel customer2 = CustomerManagementModel.builder()
                .id(1)
                .name("Test Customer")
                .phoneNumber("08123456789")
                .email("test@example.com")
                .gender("Male")
                .birthday(birthday)
                .isActive(true)
                .build();
        
        CustomerManagementModel differentCustomer = CustomerManagementModel.builder()
                .id(2)
                .name("Different Customer")
                .phoneNumber("08987654321")
                .email("different@example.com")
                .gender("Female")
                .birthday(birthday)
                .isActive(false)
                .build();
        
        assertTrue(customer1.equals(customer2));
        assertTrue(customer2.equals(customer1));
        assertFalse(customer1.equals(differentCustomer));
        assertFalse(customer1.equals(null));
        assertFalse(customer1.equals(new Object()));
    }
    
    @Test
    void testCanEqualMethod() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .phoneNumber("08123456789")
                .build();
        
        // Test the canEqual method
        assertTrue(customer.canEqual(new CustomerManagementModel()));
        assertFalse(customer.canEqual(new Object()));
        assertFalse(customer.canEqual(null));
    }
    
    @Test
    void testToStringMethod() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .id(1)
                .name("Test Customer")
                .phoneNumber("08123456789")
                .email("test@example.com")
                .gender("Male")
                .birthday(new Date())
                .isActive(true)
                .build();
        
        String toString = customer.toString();
        assertNotNull(toString);
        
        // Check that toString contains important fields
        assertTrue(toString.contains("name"));
        assertTrue(toString.contains("Test Customer"));
        assertTrue(toString.contains("phoneNumber"));
        assertTrue(toString.contains("08123456789"));
        assertTrue(toString.contains("email"));
        assertTrue(toString.contains("test@example.com"));
    }
    
    @Test
    void testSetterMethods() {
        CustomerManagementModel customer = new CustomerManagementModel();
        Date birthday = new Date();
        
        customer.setId(1);
        customer.setName("Test Customer");
        customer.setPhoneNumber("08123456789");
        customer.setEmail("test@example.com");
        customer.setGender("Male");
        customer.setBirthday(birthday);
        customer.setActive(true);
        
        assertEquals(Integer.valueOf(1), customer.getId());
        assertEquals("Test Customer", customer.getName());
        assertEquals("08123456789", customer.getPhoneNumber());
        assertEquals("test@example.com", customer.getEmail());
        assertEquals("Male", customer.getGender());
        assertEquals(birthday, customer.getBirthday());
        assertTrue(customer.isActive());
    }
}

