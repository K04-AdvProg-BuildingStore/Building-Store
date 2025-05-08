package id.ac.ui.cs.advprog.buildingstore.salesTransaction.controller;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesItemResponse;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionCreateRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionResponse;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.service.SalesTransactionService;
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

    @GetMapping
    public ResponseEntity<List<SalesTransactionResponse>> getAllTransactions() {
        List<SalesTransactionResponse> response = StreamSupport.stream(transactionService.findAll().spliterator(), false)
                .map(this::toDto)
                .toList();

        return ResponseEntity.ok(response);
    }

    private final UserRepository userRepository;

    @PostMapping
    public ResponseEntity<SalesTransactionResponse> createTransaction(@RequestBody SalesTransactionCreateRequest request) {
        User cashier = userRepository.findById(request.getCashierId())
                .orElseThrow(() -> new EntityNotFoundException("Cashier not found"));

        SalesTransaction created = transactionService.createTransaction(
                cashier,
                request.getCustomerPhone(),
                request.getStatus(),
                request.getItems()
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

        // Convert SalesItemRequest list to SalesItem list
        List<SalesItem> salesItems = request.getItems().stream().map(itemReq ->
                SalesItem.builder()
                        .quantity(itemReq.getQuantity())
                        .price(itemReq.getPrice())
                        .build()
        ).toList();

        SalesTransaction updated = transactionService.updateTransaction(
                id,
                cashier,
                request.getCustomerPhone(),
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

//    private SalesTransactionResponse toDto(SalesTransaction tx) {
//        return SalesTransactionResponse.builder()
//                .id(tx.getId())
//                .customerPhone(tx.getCustomerPhone())
//                .status(tx.getStatus())
//                .cashierUsername(tx.getCashier() != null ? tx.getCashier().getUsername() : null)
//                .build();
//    }

    private SalesTransactionResponse toDto(SalesTransaction tx) {
        return SalesTransactionResponse.builder()
                .id(tx.getId())
                .customerPhone(tx.getCustomerPhone())
                .status(tx.getStatus())
                .cashierUsername(tx.getCashier() != null ? tx.getCashier().getUsername() : null)
                .items(tx.getItems().stream().map(item ->
                        new SalesItemResponse(item.getId(), item.getTransaction().getId(), item.getQuantity(), item.getPrice())
                ).toList())
                .build();
    }
}
