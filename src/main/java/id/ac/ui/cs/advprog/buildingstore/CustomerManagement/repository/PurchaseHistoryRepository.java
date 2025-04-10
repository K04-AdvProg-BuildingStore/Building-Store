package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryModel, Integer> {

    // Get all purchase history entries for a specific customer by phone
    List<PurchaseHistoryModel> findByPhoneNumber(String phoneNumber);
}
