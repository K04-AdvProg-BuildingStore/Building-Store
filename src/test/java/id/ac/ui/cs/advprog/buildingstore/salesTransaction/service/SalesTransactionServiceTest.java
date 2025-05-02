package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesTransactionRepository;
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class SalesTransactionServiceTest {

    private SalesTransactionRepository salesTransactionRepository;
    private UserRepository userRepository;
    private SalesTransactionService salesTransactionService;

    @BeforeEach
    void setUp() {
        salesTransactionRepository = mock(SalesTransactionRepository.class);
        userRepository = mock(UserRepository.class);
        salesTransactionService = new SalesTransactionService(salesTransactionRepository, userRepository);
    }

    @Test
    void testCreateTransaction() {
        User cashier = User.builder()
                .id(1)
                .username("mcflurrins")
                .password("password")
                .role(Role.CASHIER)
                .build();

        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(cashier)
                .customerPhone(123456789)
                .status("PENDING")
                .build();

        when(salesTransactionRepository.save(any(SalesTransaction.class))).thenReturn(transaction);

        SalesTransaction result = salesTransactionService.createTransaction(cashier, 123456789, "PENDING");

        assertThat(result.getCashier()).isEqualTo(cashier);
        assertThat(result.getCustomerPhone()).isEqualTo(123456789);
        assertThat(result.getStatus()).isEqualTo("PENDING");

        verify(salesTransactionRepository).save(any(SalesTransaction.class));
    }

    @Test
    void testFindById_WhenFound() {
        SalesTransaction transaction = new SalesTransaction();
        transaction.setId(1);
        when(salesTransactionRepository.findById(1)).thenReturn(Optional.of(transaction));

        Optional<SalesTransaction> result = salesTransactionService.findById(1);

        assertThat(result).isPresent();
        assertThat(result.get().getId()).isEqualTo(1);
    }

    @Test
    void testFindById_WhenNotFound() {
        when(salesTransactionRepository.findById(1)).thenReturn(Optional.empty());

        Optional<SalesTransaction> result = salesTransactionService.findById(1);

        assertThat(result).isNotPresent();
    }

    @Test
    void testUpdateTransaction_WhenFound() {
        SalesTransaction existing = SalesTransaction.builder().id(1).status("PENDING").build();
        SalesTransaction updated = SalesTransaction.builder().status("PAID").build();

        when(salesTransactionRepository.findById(1)).thenReturn(Optional.of(existing));
        when(salesTransactionRepository.save(any())).thenReturn(existing);

        SalesTransaction result = salesTransactionService.updateTransaction(1, updated);

        assertThat(result.getStatus()).isEqualTo("PAID");
        verify(salesTransactionRepository).save(existing);
    }

    @Test
    void testUpdateTransaction_WhenNotFound() {
        SalesTransaction updated = SalesTransaction.builder().status("PAID").build();

        when(salesTransactionRepository.findById(1)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> salesTransactionService.updateTransaction(1, updated))
                .isInstanceOf(EntityNotFoundException.class)
                .hasMessage("Transaction not found");
    }

    @Test
    void testDeleteTransaction() {
        doNothing().when(salesTransactionRepository).deleteById(1);
        salesTransactionService.deleteTransaction(1);
        verify(salesTransactionRepository).deleteById(1);
    }

    @Test
    void testFindAll() {
        salesTransactionService.findAll();
        verify(salesTransactionRepository).findAll();
    }
}
