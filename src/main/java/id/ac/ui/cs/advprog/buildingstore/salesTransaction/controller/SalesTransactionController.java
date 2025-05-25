package id.ac.ui.cs.advprog.buildingstore.salesTransaction.controller;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesItemResponse;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionCreateRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionResponse;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.service.SalesTransactionService;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.CustomerManagementRepository;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;
import java.util.stream.StreamSupport;

@RestController
@RequestMapping("/api/sales-transactions")
@RequiredArgsConstructor
public class SalesTransactionController {

    private final SalesTransactionService transactionService;
    private final UserRepository userRepository;
    private final CustomerManagementRepository customerRepository;
    private final ProductManagementRepository productRepository;

    @GetMapping
    public ResponseEntity<List<SalesTransactionResponse>> getAllTransactions() {
        List<SalesTransactionResponse> response = StreamSupport.stream(transactionService.findAll().spliterator(), false)
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    @PostMapping
    public ResponseEntity<SalesTransactionResponse> createTransaction(@RequestBody SalesTransactionCreateRequest request) {
        User cashier = userRepository.findById(request.getCashierId())
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found"));

        CustomerManagementModel customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        SalesTransaction created = transactionService.createTransaction(
                cashier,
                customer,
                request.getStatus(),
                request.getItems() == null ? List.of() : request.getItems()
        );

        return ResponseEntity.ok(toDto(created));
    }

    @GetMapping("/{id}")
    public ResponseEntity<SalesTransactionResponse> getTransaction(@PathVariable Integer id) {
        Optional<SalesTransaction> transaction = transactionService.findById(id);
        return transaction.map(t -> ResponseEntity.ok(toDto(t)))
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<SalesTransactionResponse> updateTransaction(
            @PathVariable Integer id,
            @RequestBody SalesTransactionCreateRequest request) {

        User cashier = userRepository.findById(request.getCashierId())
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found"));

        CustomerManagementModel customer = customerRepository.findById(request.getCustomerId())
                .orElseThrow(() -> new EntityNotFoundException("Customer not found"));

        // Convert SalesItemRequest list to SalesItem list, including product
        List<SalesItem> salesItems = (request.getItems() == null ? List.<id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesItemRequest>of() : request.getItems())
                .stream().map(itemReq -> {
                    ProductManagementModel product = null;
                    if (itemReq.getProductId() != null) {
                        product = productRepository.findById(itemReq.getProductId())
                                .orElseThrow(() -> new EntityNotFoundException("Product not found"));
                    }
                    return SalesItem.builder()
                            .product(product)
                            .quantity(itemReq.getQuantity())
                            .price(itemReq.getPrice())
                            .build();
                }).toList();

        SalesTransaction updated = transactionService.updateTransaction(
                id,
                cashier,
                customer,
                request.getStatus(),
                salesItems
        );

        return ResponseEntity.ok(toDto(updated));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteTransaction(@PathVariable Integer id) {
        transactionService.deleteTransaction(id);
        return ResponseEntity.noContent().build();
    }

    private SalesTransactionResponse toDto(SalesTransaction tx) {
        return SalesTransactionResponse.builder()
                .id(tx.getId())
                .customerId(tx.getCustomer().getId())
                .cashierId(tx.getCashier() != null ? tx.getCashier().getId() : null) // Add this line
                .status(tx.getStatus())
                .cashierUsername(tx.getCashier() != null ? tx.getCashier().getUsername() : null)
                .items((tx.getItems() == null ? List.<SalesItem>of() : tx.getItems()).stream().map(item ->
                        new SalesItemResponse(
                                item.getProduct() != null ? item.getProduct().getId() : 0,
                                item.getTransaction() != null ? item.getTransaction().getId() : 0,
                                item.getQuantity(),
                                item.getPrice()
                        )
                ).toList())
                .build();
    }
}

