package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class SupplierManagementRepositoryTest {

    @Autowired
    private SupplierManagementRepository repository;

    private SupplierManagementModel supplier;

    @BeforeEach
    void setUp() {
        supplier = new SupplierManagementModel();
        supplier.setName("PT. Sumber Rejeki");
        supplier.setPhoneNumber("08123456789");
        supplier.setAddress("Jakarta");
        supplier.setActive(true);
    }

    @Test
    void testSaveAndFindByPhoneNumber() {
        repository.save(supplier);

        Optional<SupplierManagementModel> found = repository.findByPhoneNumber("08123456789");

        assertTrue(found.isPresent());
        assertEquals("PT. Sumber Rejeki", found.get().getName());
        assertEquals("Jakarta", found.get().getAddress());
    }

    @Test
    void testDeleteByPhoneNumber() {
        repository.save(supplier);

        repository.deleteByPhoneNumber("08123456789");

        Optional<SupplierManagementModel> found = repository.findByPhoneNumber("08123456789");
        assertFalse(found.isPresent());
    }

    @Test
    void testFindByPhoneNumberNotFound() {
        Optional<SupplierManagementModel> found = repository.findByPhoneNumber("000");
        assertFalse(found.isPresent());
    }
}
