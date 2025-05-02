package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesTransactionService {

    private final SalesTransactionRepository salesTransactionRepository;
    private final UserRepository userRepository; // Inject this


    public SalesTransaction createTransaction(User cashier, int customerPhone, String status) {
        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(cashier)
                .customerPhone(customerPhone)
                .status(status)
                .build();

        return salesTransactionRepository.save(transaction);
    }
    
    public Optional<SalesTransaction> findById(Integer id) {
        return salesTransactionRepository.findById(id);
    }

    public SalesTransaction updateTransaction(Integer id, SalesTransaction updatedTransaction) {
        SalesTransaction existing = salesTransactionRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Transaction not found"));

        existing.setCashier(updatedTransaction.getCashier());
        existing.setCustomerPhone(updatedTransaction.getCustomerPhone());
        existing.setStatus(updatedTransaction.getStatus());

        return salesTransactionRepository.save(existing);
    }

    public void deleteTransaction(Integer id) {
        salesTransactionRepository.deleteById(id);
    }

    public Iterable<SalesTransaction> findAll() {
        return salesTransactionRepository.findAll();
    }

}
