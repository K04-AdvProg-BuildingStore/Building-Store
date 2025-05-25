package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SalesTransactionRepository extends JpaRepository<SalesTransaction, Integer> {
    @EntityGraph(value = "SalesTransaction.withCashierCustomerItemsProduct", type = EntityGraph.EntityGraphType.LOAD)
    List<SalesTransaction> findAll();
}