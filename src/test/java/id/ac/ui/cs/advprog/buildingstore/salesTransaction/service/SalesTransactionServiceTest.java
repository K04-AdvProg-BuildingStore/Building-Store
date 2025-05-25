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
import jakarta.persistence.EntityNotFoundException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

class SalesTransactionServiceTest {

    @Mock
    private SalesTransactionRepository salesTransactionRepository;

    @Mock
    private SalesItemRepository salesItemRepository;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SalesTransactionService salesTransactionService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    void testCreateTransactionSuccessfully() {
        User cashier = User.builder().id(1).username("john").build();
        CustomerManagementModel customer = CustomerManagementModel.builder().id(812345678).build();
        List<SalesItemRequest> itemRequests = List.of(
                new SalesItemRequest(null, null, 2, 10000),
                new SalesItemRequest(null, null, 1, 20000)
        );

        SalesTransaction savedTx = SalesTransaction.builder()
                .id(1)
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PAID)
                .items(new ArrayList<>())
                .build();

        when(salesTransactionRepository.save(any(SalesTransaction.class))).thenReturn(savedTx);
        when(salesItemRepository.save(any(SalesItem.class))).thenAnswer(i -> i.getArgument(0));

        SalesTransaction result = salesTransactionService.createTransaction(cashier, customer, TransactionStatus.PAID, itemRequests);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        verify(salesTransactionRepository, times(1)).save(any(SalesTransaction.class));
        verify(salesItemRepository, times(2)).save(any(SalesItem.class));
    }

    @Test
    void testUpdateTransactionSuccessfully() {
        User cashier = User.builder().id(2).username("jane").build();
        CustomerManagementModel customer = CustomerManagementModel.builder().id(800000000).build();
        SalesTransaction existing = SalesTransaction.builder()
                .id(10)
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        List<SalesItem> updatedItems = List.of(
                SalesItem.builder().quantity(1).price(50000).build(),
                SalesItem.builder().quantity(3).price(10000).build()
        );

        when(salesTransactionRepository.findById(10)).thenReturn(Optional.of(existing));
        when(salesTransactionRepository.save(any(SalesTransaction.class))).thenAnswer(i -> i.getArgument(0));

        SalesTransaction result = salesTransactionService.updateTransaction(10, cashier, customer, TransactionStatus.PAID, updatedItems);

        assertNotNull(result);
        assertEquals(TransactionStatus.PAID, result.getStatus());
        assertEquals(2, result.getItems().size());
    }

    @Test
    void testUpdateTransactionThrowsWhenNotFound() {
        when(salesTransactionRepository.findById(999)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class, () ->
                salesTransactionService.updateTransaction(999, null, null, TransactionStatus.PENDING, new ArrayList<>()));
    }

    @Test
    void testDeleteTransactionSuccessfully() {
        doNothing().when(salesTransactionRepository).deleteById(1);

        salesTransactionService.deleteTransaction(1);

        verify(salesTransactionRepository, times(1)).deleteById(1);
    }
}

