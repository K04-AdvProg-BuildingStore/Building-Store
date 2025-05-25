package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplierManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

public class SupplierManagementServiceTest {

    private SupplierManagementService service;
    private SupplierManagementRepository repository;

    private SupplierManagementModel supplier;

    @BeforeEach
    void setUp() {
        repository = mock(SupplierManagementRepository.class);
        service = new SupplierManagementService(repository);

        supplier = new SupplierManagementModel();
        supplier.setId(1);
        supplier.setName("PT. Sumber Rejeki");
        supplier.setPhoneNumber("08123456789");
        supplier.setAddress("Jakarta");
        supplier.setActive(true);
    }

    @Test
    void testAddSupplierSuccess() {
        when(repository.save(any())).thenReturn(supplier);

        SupplierManagementModel result = service.addSupplier(supplier);

        assertNotNull(result);
        assertEquals("PT. Sumber Rejeki", result.getName());
        verify(repository, times(1)).save(supplier);
    }

    @Test
    void testAddSupplierFailWhenPhoneIsNull() {
        supplier.setPhoneNumber(null);

        SupplierManagementModel result = service.addSupplier(supplier);

        assertNull(result);
        verify(repository, never()).save(any());
    }

    @Test
    void testGetSupplierByPhoneFound() {
        when(repository.findByPhoneNumber("08123456789")).thenReturn(Optional.of(supplier));

        Optional<SupplierManagementModel> result = service.getSupplierByPhone("08123456789");

        assertTrue(result.isPresent());
        assertEquals("Jakarta", result.get().getAddress());
    }

    @Test
    void testGetSupplierByPhoneNotFound() {
        when(repository.findByPhoneNumber("000")).thenReturn(Optional.empty());

        Optional<SupplierManagementModel> result = service.getSupplierByPhone("000");

        assertFalse(result.isPresent());
    }

    @Test
    void testDeleteSupplierByPhone() {
        doNothing().when(repository).deleteByPhoneNumber("08123456789");

        service.deleteSupplierByPhone("08123456789");

        verify(repository, times(1)).deleteByPhoneNumber("08123456789");
    }

    @Test
    void testUpdateSupplierInfoSuccess() {
        when(repository.findByPhoneNumber("08123456789")).thenReturn(Optional.of(supplier));
        when(repository.save(any())).thenReturn(supplier);

        SupplierManagementModel result = service.updateSupplierInfo("08123456789", "PT. Baru", "Bandung", false);

        assertNotNull(result);
        assertEquals("PT. Baru", result.getName());
        assertEquals("Bandung", result.getAddress());
        assertFalse(result.isActive());
    }

    @Test
    void testUpdateSupplierInfoNotFound() {
        when(repository.findByPhoneNumber("000")).thenReturn(Optional.empty());

        SupplierManagementModel result = service.updateSupplierInfo("000", "A", "B", true);

        assertNull(result);
        verify(repository, never()).save(any());
    }
}