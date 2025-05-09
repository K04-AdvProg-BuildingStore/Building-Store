package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplyRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import java.util.List;

@Service
public class SupplyService {

    @Autowired
    private SupplyRepository supplyRepo;

    @Autowired
    private SupplierManagementService supplierService;

    public List<SupplyModel> getAllSupplies() {
        return supplyRepo.findAll();
    }

    public SupplyModel getSupplyById(Integer id) {
        return supplyRepo.findById(id)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supply not found"));
    }

    public List<SupplyModel> getSuppliesBySupplierPhone(String phone) {
        SupplierManagementModel sup = supplierService
                .getSupplierByPhone(phone)
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.NOT_FOUND, "Supplier not found"));
        return supplyRepo.findBySupplierId(sup.getId());
    }

//    public List<SupplyModel> getSuppliesByProduct(Integer productId) {
//        return supplyRepo.findByProductId(productId);
//    }

    public SupplyModel createSupply(SupplyModel supply) {
        SupplierManagementModel sup = supplierService
                .getSupplierByPhone(supply.getSupplier().getPhoneNumber())
                .orElseThrow(() -> new ResponseStatusException(
                        HttpStatus.BAD_REQUEST, "Supplier not found"));
        supply.setSupplier(sup);
        return supplyRepo.save(supply);
    }

    /** Update an existing restock event */
    public SupplyModel updateSupply(Integer id, SupplyModel updates) {
        SupplyModel existing = getSupplyById(id);

        if (updates.getSupplier() != null) {
            SupplierManagementModel sup = supplierService
                    .getSupplierByPhone(updates.getSupplier().getPhoneNumber())
                    .orElseThrow(() -> new ResponseStatusException(
                            HttpStatus.BAD_REQUEST, "Supplier not found"));
            existing.setSupplier(sup);
        }
        if (updates.getSupplyStock() != null) {
            existing.setSupplyStock(updates.getSupplyStock());
        }
        if (updates.getDeliveryAddress() != null) {
            existing.setDeliveryAddress(updates.getDeliveryAddress());
        }

        return supplyRepo.save(existing);
    }

    public void deleteSupply(Integer id) {
        supplyRepo.deleteById(id);
    }
}
