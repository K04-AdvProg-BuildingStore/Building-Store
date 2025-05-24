package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesItemRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesItemRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesTransactionRepository;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesTransactionService {

    private final SalesTransactionRepository salesTransactionRepository;
    private final SalesItemRepository salesItemRepository;
    private final UserRepository userRepository;
    private final ProductManagementRepository productRepository;

    public SalesTransaction createTransaction(User cashier, CustomerManagementModel customer, TransactionStatus status, List<SalesItemRequest> items) {
        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(cashier)
                .customer(customer)
                .status(status)
                .items(new ArrayList<>())
                .build();

        SalesTransaction savedTransaction = salesTransactionRepository.save(transaction);

        for (SalesItemRequest item : items) {
            ProductManagementModel product = null;
            if (item.getProductId() != null) {
                product = productRepository.findById(item.getProductId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));
            }
            SalesItem newItem = SalesItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(item.getPrice())
                    .transaction(savedTransaction)
                    .build();
            savedTransaction.getItems().add(salesItemRepository.save(newItem));
        }

        return savedTransaction;
    }

    @Transactional
    public SalesTransaction updateTransaction(Integer id, User cashier, CustomerManagementModel customer, TransactionStatus status, List<SalesItem> items) {
        SalesTransaction existing = salesTransactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        existing.setCashier(cashier);
        existing.setCustomer(customer);
        existing.setStatus(status);

        // Clear and replace items
        existing.getItems().clear();
        if (items != null) {
            for (SalesItem item : items) {
                item.setTransaction(existing);
                existing.getItems().add(item);
            }
        }

        return salesTransactionRepository.save(existing);
    }

    public Optional<SalesTransaction> findById(Integer id) {
        return salesTransactionRepository.findById(id);
    }

    public void deleteTransaction(Integer id) {
        salesTransactionRepository.deleteById(id);
    }

    public Iterable<SalesTransaction> findAll() {
        return salesTransactionRepository.findAll();
    }
}
