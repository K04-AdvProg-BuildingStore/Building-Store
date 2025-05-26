package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplyRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class SupplyServiceTest {

    @Mock
    private SupplyRepository supplyRepo;

    @Mock
    private SupplierManagementService supplierService;

    @InjectMocks
    private SupplyService service;

    private SupplierManagementModel sampleSupplier;
    private SupplyModel sampleSupply;

    @BeforeEach
    void setUp() {
        sampleSupplier = new SupplierManagementModel();
        sampleSupplier.setId(1);
        sampleSupplier.setName("Supplier");
        sampleSupplier.setPhoneNumber("555-1234");
        sampleSupplier.setAddress("Address");
        sampleSupplier.setActive(true);

        sampleSupply = new SupplyModel();
        sampleSupply.setId(10);
        sampleSupply.setSupplier(sampleSupplier);
        sampleSupply.setSupplyStock(100);
        sampleSupply.setDeliveryAddress("Warehouse");
    }

    @Test
    void testGetAllSupplies() {
        when(supplyRepo.findAll()).thenReturn(List.of(sampleSupply));
        List<SupplyModel> result = service.getAllSupplies();
        assertEquals(1, result.size());
        assertEquals(sampleSupply, result.get(0));
        verify(supplyRepo).findAll();
    }

    @Test
    void testGetSupplyByIdFound() {
        when(supplyRepo.findById(10)).thenReturn(Optional.of(sampleSupply));
        SupplyModel result = service.getSupplyById(10);
        assertEquals(sampleSupply, result);
        verify(supplyRepo).findById(10);
    }

    @Test
    void testGetSupplyByIdNotFound() {
        when(supplyRepo.findById(11)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class, () -> service.getSupplyById(11));
        verify(supplyRepo).findById(11);
    }

    @Test
    void testGetSuppliesBySupplierPhoneFound() {
        when(supplierService.getSupplierByPhone("555-1234"))
                .thenReturn(Optional.of(sampleSupplier));
        when(supplyRepo.findBySupplierId(1))
                .thenReturn(List.of(sampleSupply));
        List<SupplyModel> result = service.getSuppliesBySupplierPhone("555-1234");
        assertEquals(1, result.size());
        assertEquals(sampleSupply, result.get(0));
        verify(supplierService).getSupplierByPhone("555-1234");
        verify(supplyRepo).findBySupplierId(1);
    }

    @Test
    void testGetSuppliesBySupplierPhoneNotFound() {
        when(supplierService.getSupplierByPhone("000-0000"))
                .thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,
                () -> service.getSuppliesBySupplierPhone("000-0000"));
        verify(supplierService).getSupplierByPhone("000-0000");
        verifyNoMoreInteractions(supplyRepo);
    }

    @Test
    void testGetSuppliesByProduct() {
        when(supplyRepo.findByProductId(5))
                .thenReturn(List.of(sampleSupply));
        List<SupplyModel> result = service.getSuppliesByProduct(5);
        assertEquals(1, result.size());
        assertEquals(sampleSupply, result.get(0));
        verify(supplyRepo).findByProductId(5);
    }

    @Test
    void testCreateSupplySupplierNotFound() {
        SupplyModel toCreate = new SupplyModel();
        SupplierManagementModel stubSupplier = new SupplierManagementModel();
        stubSupplier.setPhoneNumber("000-0000");
        toCreate.setSupplier(stubSupplier);

        when(supplierService.getSupplierByPhone("000-0000"))
                .thenReturn(Optional.empty());

        assertThrows(ResponseStatusException.class,
                () -> service.createSupply(toCreate));
        verify(supplierService).getSupplierByPhone("000-0000");
        verifyNoMoreInteractions(supplyRepo);
    }

    @Test
    void testUpdateSupplyChanges() {
        when(supplyRepo.findById(10)).thenReturn(Optional.of(sampleSupply));
        SupplyModel updates = new SupplyModel();
        SupplierManagementModel newSup = new SupplierManagementModel();
        newSup.setPhoneNumber("555-1234");
        updates.setSupplier(newSup);
        updates.setSupplyStock(200);
        updates.setDeliveryAddress("NewAddr");

        when(supplierService.getSupplierByPhone("555-1234"))
                .thenReturn(Optional.of(sampleSupplier));
        when(supplyRepo.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        SupplyModel result = service.updateSupply(10, updates);
        assertEquals(200, result.getSupplyStock());
        assertEquals("NewAddr", result.getDeliveryAddress());
        assertEquals(sampleSupplier, result.getSupplier());
        verify(supplyRepo).findById(10);
        verify(supplierService).getSupplierByPhone("555-1234");
        verify(supplyRepo).save(result);
    }

    @Test
    void testUpdateSupplyNotFound() {
        when(supplyRepo.findById(11)).thenReturn(Optional.empty());
        assertThrows(ResponseStatusException.class,
                () -> service.updateSupply(11, new SupplyModel()));
        verify(supplyRepo).findById(11);
    }

    @Test
    void testDeleteSupply() {
        service.deleteSupply(10);
        verify(supplyRepo).deleteById(10);
    }
}