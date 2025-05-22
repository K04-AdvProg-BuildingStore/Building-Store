package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

public interface SupplierManagementRepository extends JpaRepository<SupplierManagementModel, Integer> {

    Optional<SupplierManagementModel> findByPhoneNumber(String phoneNumber);

    @Transactional
    void deleteByPhoneNumber(String phoneNumber);
}