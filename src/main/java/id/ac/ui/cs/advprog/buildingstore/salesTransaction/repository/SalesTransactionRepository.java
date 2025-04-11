package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Integer> {
}