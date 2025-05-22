package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SupplyRepository extends JpaRepository<SupplyModel, Integer> {
    List<SupplyModel> findBySupplierId(Integer supplierId);
  //  List<SupplyModel> findByProductId(Integer productId);
}
