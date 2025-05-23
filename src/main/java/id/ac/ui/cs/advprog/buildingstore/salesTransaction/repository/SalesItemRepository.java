package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesItemRepository extends JpaRepository<SalesItem, Integer> {
    void deleteAllByTransactionId(Integer transactionId);

}
