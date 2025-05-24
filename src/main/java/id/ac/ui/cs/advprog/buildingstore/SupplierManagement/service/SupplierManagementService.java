package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplierManagementRepository;

@Service
public class SupplierManagementService {

    private final SupplierManagementRepository repository;

    public SupplierManagementService(SupplierManagementRepository repository) {
        this.repository = repository;
    }

    public SupplierManagementModel addSupplier(SupplierManagementModel supplier){
        if (supplier.getPhoneNumber() == null || supplier.getPhoneNumber().isBlank()){
            return null;
        }
        return repository.save(supplier);
    }

    public Optional<SupplierManagementModel> getSupplierByPhone(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    public List<SupplierManagementModel> getAllSuppliers() {
        return repository.findAll();
    }

    public void deleteSupplierByPhone(String phoneNumber) {
        repository.deleteByPhoneNumber(phoneNumber);
    }

    public SupplierManagementModel updateSupplierInfo(String phoneNumber, String name, String address, Boolean isActive) {
        Optional<SupplierManagementModel> optionalSupplier = repository.findByPhoneNumber(phoneNumber);

        if (optionalSupplier.isPresent()) {
            SupplierManagementModel supplier = optionalSupplier.get();

            if (name != null) supplier.setName(name);
            if (address != null) supplier.setAddress(address);
            if (isActive != null) supplier.setActive(isActive);

            return repository.save(supplier);
        }

        return null;
    }
}