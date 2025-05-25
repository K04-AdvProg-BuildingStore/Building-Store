package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.CustomerManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
public class CustomerManagementServiceTest {
    
    private CustomerManagementService service;
    private CustomerManagementRepository repository;


    @BeforeEach
    void setUp() {
        repository = mock(CustomerManagementRepository.class);
        service = new CustomerManagementService(repository);
    }

    @Test
    void testAddCustomerSuccess() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .phoneNumber("08123456789")
                .name("Alice")
                .build();

        when(repository.save(customer)).thenReturn(customer);

        CustomerManagementModel result = service.addCustomer(customer);
        assertNotNull(result);
        assertEquals("Alice", result.getName());
        verify(repository, times(1)).save(customer);
    }


    @Test
    void testAddCustomerWithNullPhoneShouldNotBeSaved() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("Anonymous")
                .phoneNumber(null)
                .build();

        CustomerManagementModel result = service.addCustomer(customer);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testGetCustomerByPhoneNumber() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .phoneNumber("08123456789")
                .name("Alice")
                .build();

        when(repository.findByPhoneNumber("08123456789")).thenReturn(Optional.of(customer));

        Optional<CustomerManagementModel> result = service.getCustomerByPhone("08123456789");
        assertTrue(result.isPresent());
        assertEquals("Alice", result.get().getName());
    }

    @Test
    void testDeleteCustomerByPhoneNumber() {
        service.deleteCustomerByPhone("08123456789");
        verify(repository, times(1)).deleteByPhoneNumber("08123456789");
    }

    @Test
    void testUpdateCustomerInfoSuccess() {
        String phoneNumber = "08123456789";

        CustomerManagementModel existing = CustomerManagementModel.builder()
                .phoneNumber(phoneNumber)
                .name("Old Name")
                .email("old@email.com")
                .gender("Other")
                .birthday(new Date())
                .isActive(true)
                .build();

        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerManagementModel updated = service.updateCustomerInfo(
                phoneNumber,
                "New Name",
                "new@email.com",
                "Female",
                new Date(),
                false
        );

        assertNotNull(updated);
        assertEquals("New Name", updated.getName());
        assertEquals("new@email.com", updated.getEmail());
        assertEquals("Female", updated.getGender());
        assertFalse(updated.isActive());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateCustomerInfoCustomerNotFound() {
        when(repository.findByPhoneNumber("0000")).thenReturn(Optional.empty());

        CustomerManagementModel result = service.updateCustomerInfo(
                "0000", "Name", "mail", "Gender", new Date(), true
        );

        assertNull(result);
        verify(repository, never()).save(any());
    }
    @Test
    void testAddCustomerWithBlankPhoneShouldNotBeSaved() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("Anonymous")
                .phoneNumber("   ")
                .build();

        CustomerManagementModel result = service.addCustomer(customer);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testUpdateCustomerInfoWithAllNullExceptPhone() {
        String phoneNumber = "08123456789";
        Date birthday = new Date();
        CustomerManagementModel existing = CustomerManagementModel.builder()
                .phoneNumber(phoneNumber)
                .name("Name")
                .email("mail@mail.com")
                .gender("Male")
                .birthday(birthday)
                .isActive(true)
                .build();

        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerManagementModel updated = service.updateCustomerInfo(
                phoneNumber, null, null, null, null, null
        );

        assertNotNull(updated);
        assertEquals("Name", updated.getName());
        assertEquals("mail@mail.com", updated.getEmail());
        assertEquals("Male", updated.getGender());
        assertEquals(birthday, updated.getBirthday());
        assertTrue(updated.isActive());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateCustomerInfoWithSomeNullParameters() {
        String phoneNumber = "08123456789";
        Date birthday = new Date();
        CustomerManagementModel existing = CustomerManagementModel.builder()
                .phoneNumber(phoneNumber)
                .name("Old Name")
                .email("old@email.com")
                .gender("Other")
                .birthday(birthday)
                .isActive(true)
                .build();

        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerManagementModel updated = service.updateCustomerInfo(
                phoneNumber, "New Name", null, null, null, null
        );

        assertNotNull(updated);
        assertEquals("New Name", updated.getName());
        assertEquals("old@email.com", updated.getEmail());
        assertEquals("Other", updated.getGender());
        assertEquals(birthday, updated.getBirthday());
        assertTrue(updated.isActive());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateCustomerInfoWithIsActiveTrue() {
        String phoneNumber = "08123456789";
        CustomerManagementModel existing = CustomerManagementModel.builder()
                .phoneNumber(phoneNumber)
                .isActive(false)
                .build();

        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerManagementModel updated = service.updateCustomerInfo(
                phoneNumber, null, null, null, null, true
        );

        assertNotNull(updated);
        assertTrue(updated.isActive());
        verify(repository).save(existing);
    }

    @Test
    void testUpdateCustomerInfoWithBirthdayNull() {
        String phoneNumber = "08123456789";
        Date birthday = new Date();
        CustomerManagementModel existing = CustomerManagementModel.builder()
                .phoneNumber(phoneNumber)
                .birthday(birthday)
                .build();

        when(repository.findByPhoneNumber(phoneNumber)).thenReturn(Optional.of(existing));
        when(repository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        CustomerManagementModel updated = service.updateCustomerInfo(
                phoneNumber, null, null, null, null, null
        );

        assertNotNull(updated);
        assertEquals(birthday, updated.getBirthday());
        verify(repository).save(existing);
    }

    @Test
    void testGetAllCustomers() {
        // Create test data
        CustomerManagementModel customer1 = CustomerManagementModel.builder()
                .phoneNumber("08123456789")
                .name("Alice")
                .build();
                
        CustomerManagementModel customer2 = CustomerManagementModel.builder()
                .phoneNumber("08123456788")
                .name("Bob")
                .build();

        // Set up mock repository behavior
        when(repository.findAll()).thenReturn(List.of(customer1, customer2));

        // Call the service method
        List<CustomerManagementModel> result = service.getAllCustomers();

        // Verify the results
        assertNotNull(result);
        assertEquals(2, result.size());
        assertEquals("Alice", result.get(0).getName());
        assertEquals("Bob", result.get(1).getName());
        verify(repository, times(1)).findAll();
    }

    @Test
    void testGetAllCustomersEmptyList() {
        // Set up mock repository to return empty list
        when(repository.findAll()).thenReturn(List.of());

        // Call the service method
        List<CustomerManagementModel> result = service.getAllCustomers();

        // Verify the results
        assertNotNull(result);
        assertTrue(result.isEmpty());
        verify(repository, times(1)).findAll();
    }

}
