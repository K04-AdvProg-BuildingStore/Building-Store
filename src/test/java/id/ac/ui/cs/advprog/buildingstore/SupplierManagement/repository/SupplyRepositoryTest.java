package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
class SupplyRepositoryTest {

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private SupplierManagementRepository supplierRepository;

    @Test
    void testFindBySupplierId() {
        // given
        SupplierManagementModel supplier = new SupplierManagementModel();
        supplier.setName("Test Supplier");
        supplier.setPhoneNumber("555-0001");
        supplier.setAddress("Test Address");
        supplier.setActive(true);
        supplier = supplierRepository.saveAndFlush(supplier);

        SupplyModel supply = new SupplyModel();
        supply.setSupplier(supplier);
        supply.setSupplyStock(20);
        supply.setDeliveryAddress("Main Warehouse");
        supply = supplyRepository.saveAndFlush(supply);

        // when
        List<SupplyModel> found = supplyRepository.findBySupplierId(supplier.getId());

        // then
        assertThat(found).hasSize(1).containsExactly(supply);
    }

    // @Test
    // void testFindByProductId() {
    //     // Uncomment and implement when findByProductId is enabled in the repository
    //     // and SupplyModel has a Product mapping.
    // }
}
