package id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;


public interface ProductManagementRepository extends JpaRepository<ProductManagementModel, Integer> {


    @Transactional
    void deleteByName(String name);
    List<ProductManagementModel> findByNameContainingIgnoreCase(String name);

}
