package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class CustomerManagementRepositoryTest {

    @Autowired
    private CustomerManagementRepository repository;

    @Test
    void testSaveAndFindByPhoneNumber() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("Jane Doe")
                .phoneNumber("08123456789")
                .isActive(true)
                .build();

        repository.save(customer);

        Optional<CustomerManagementModel> found = repository.findByPhoneNumber("08123456789");

        assertTrue(found.isPresent());
        assertEquals("Jane Doe", found.get().getName());
        assertEquals("08123456789", found.get().getPhoneNumber());
    }

    @Test
    void testDeleteByPhoneNumber() {
        CustomerManagementModel customer = CustomerManagementModel.builder()
                .name("John Doe")  // Add required fields
                .phoneNumber("08999999999")
                .isActive(true)
                .build();
    
        repository.save(customer);
        
        // Verify it exists first
        Optional<CustomerManagementModel> beforeDelete = repository.findByPhoneNumber("08999999999");
        assertTrue(beforeDelete.isPresent());
        
        // Delete
        repository.deleteByPhoneNumber("08999999999");
        
        // Verify deletion
        Optional<CustomerManagementModel> afterDelete = repository.findByPhoneNumber("08999999999");
        assertFalse(afterDelete.isPresent());
    }
}


