package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
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
    private final ProductManagementRepository productRepository;
    private final SalesTransactionMetricsService metricsService; // Add this line

    @Transactional
    public SalesTransaction createTransaction(User cashier, CustomerManagementModel customer, TransactionStatus status, List<SalesItemRequest> items) {
        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(cashier)
                .customer(customer)
                .status(status)
                .items(new ArrayList<>())
                .build();

        SalesTransaction savedTransaction = salesTransactionRepository.save(transaction);

        for (SalesItemRequest item : items) {
            ProductManagementModel product = productRepository.findById(item.getProductId())
                    .orElseThrow(() -> new EntityNotFoundException("Product not found"));

            if (product.getQuantity() == null || product.getQuantity() < item.getQuantity()) {
                throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
            }

            int calculatedPrice = product.getPrice() * item.getQuantity();

            // Reduce product stock
            product.setQuantity(product.getQuantity() - item.getQuantity());
            productRepository.save(product);

            SalesItem newItem = SalesItem.builder()
                    .product(product)
                    .quantity(item.getQuantity())
                    .price(calculatedPrice)
                    .transaction(savedTransaction)
                    .build();
            savedTransaction.getItems().add(salesItemRepository.save(newItem));
        }
        metricsService.incrementTransactionCount();

        return savedTransaction;
    }


    @Transactional
    public SalesTransaction updateTransaction(Integer id, User cashier, CustomerManagementModel customer, TransactionStatus status, List<SalesItem> items) {
        SalesTransaction existing = salesTransactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        existing.setCashier(cashier);
        existing.setCustomer(customer);
        existing.setStatus(status);

        // Restore product quantities for old items
        for (SalesItem oldItem : existing.getItems()) {
            ProductManagementModel product = oldItem.getProduct();
            if (product != null) {
                product.setQuantity(product.getQuantity() + oldItem.getQuantity());
                productRepository.save(product);
            }
        }

        existing.getItems().clear();

        if (items != null) {
            for (SalesItem item : items) {
                ProductManagementModel product = productRepository.findById(item.getProduct().getId())
                        .orElseThrow(() -> new EntityNotFoundException("Product not found"));

                if (product.getQuantity() == null || product.getQuantity() < item.getQuantity()) {
                    throw new IllegalArgumentException("Insufficient stock for product ID: " + product.getId());
                }

                int calculatedPrice = product.getPrice() * item.getQuantity();

                // Reduce product stock
                product.setQuantity(product.getQuantity() - item.getQuantity());
                productRepository.save(product);

                item.setPrice(calculatedPrice);
                item.setProduct(product);
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
