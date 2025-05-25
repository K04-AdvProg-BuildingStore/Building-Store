package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface PurchaseHistoryRepository extends JpaRepository<PurchaseHistoryModel, Integer> {

    // Get all purchase history entries for a specific customer by phone (kept for model compatibility)
    List<PurchaseHistoryModel> findByPhoneNumber(String phoneNumber);
    
    // Add this method for the full customer purchase history by ID
    @Query(value = """
        SELECT
            c.id AS customerId,
            c.name AS customerName,
            c.phone_number AS phoneNumber,
            st.id AS transactionId,
            si.product_id AS productId,
            si.quantity AS quantity,
            si.price AS price
        FROM customers c
        JOIN sales_transaction st ON c.id = st.customer_id
        JOIN sales_item si ON st.id = si.transaction_id
        WHERE c.id = :customerId
        """, nativeQuery = true)
    List<Object[]> findFullCustomerPurchaseHistoryByIdRaw(@Param("customerId") Integer customerId);
}