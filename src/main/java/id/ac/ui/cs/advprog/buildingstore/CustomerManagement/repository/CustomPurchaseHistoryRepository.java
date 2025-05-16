package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class CustomPurchaseHistoryRepository {

    @PersistenceContext
    private EntityManager entityManager;

    public List<PurchaseHistoryViewDTO> findPurchaseHistoryByPhoneNumber(String phoneNumber) {
        String sql = """
            SELECT st.id as transactionId, si.product_id as productName, si.quantity
            FROM sales_transaction st
            JOIN sales_item si ON st.id = si.transaction_id
            WHERE st.customer_phone = :phoneNumber
        """;
        return entityManager.createNativeQuery(sql, "PurchaseHistoryViewDTOMapping")
                .setParameter("phoneNumber", phoneNumber)
                .getResultList();
    }
}