package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

//import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesItemRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;
    private final SalesTransactionRepository salesTransactionRepository;

    public Optional<SalesItem> findById(String id) {
        return salesItemRepository.findById(id);
    }

    public SalesItem createSalesItem(
           // ProductManagementModel product,
            SalesTransaction transaction,
            int quantity,
            int price
    ) {
        // Check if the SalesTransaction exists
        Optional<SalesTransaction> existingTransaction = salesTransactionRepository.findById(transaction.getId());
        if (existingTransaction.isEmpty()) {
            throw new EntityNotFoundException("SalesTransaction with ID " + transaction.getId() + " not found.");
        }

        SalesItem newItem = SalesItem.builder()
               // .product(product)
                .quantity(quantity)
                .price(price)
                .transaction(transaction)
                .build();

        return salesItemRepository.save(newItem);
    }

    public Iterable<SalesItem> findAll() {
        return salesItemRepository.findAll();
    }

    public Optional<SalesItem> getSalesItemById(String id) {
        return salesItemRepository.findById(id);
    }

    public SalesItem updateSalesItem(int id, SalesTransaction transaction, int quantity, int price) {
        SalesItem existingItem = salesItemRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("SalesItem with ID " + id + " not found."));

        // Validate transaction exists
        SalesTransaction existingTransaction = salesTransactionRepository.findById(transaction.getId())
                .orElseThrow(() -> new EntityNotFoundException("SalesTransaction with ID " + transaction.getId() + " not found."));

        existingItem.setTransaction(existingTransaction);
        existingItem.setQuantity(quantity);
        existingItem.setPrice(price);

        return salesItemRepository.save(existingItem);
    }

    @Transactional
    public void deleteSalesItem(int id) {
        SalesItem item = salesItemRepository.findById(String.valueOf(id))
                .orElseThrow(() -> new EntityNotFoundException("SalesItem with ID " + id + " not found."));
        salesItemRepository.delete(item);
    }

}
