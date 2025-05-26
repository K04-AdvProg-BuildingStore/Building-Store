package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
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
    private ProductManagementRepository productRepository;

    @Mock
    private SalesTransactionMetricsService metricsService;
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

        ProductManagementModel product1 = ProductManagementModel.builder().id(101).price(10000).quantity(10).build();
        ProductManagementModel product2 = ProductManagementModel.builder().id(102).price(20000).quantity(5).build();

        List<SalesItemRequest> itemRequests = List.of(
                new SalesItemRequest(null, 101, 2),
                new SalesItemRequest(null, 102, 1)
        );

        when(productRepository.findById(101)).thenReturn(Optional.of(product1));
        when(productRepository.findById(102)).thenReturn(Optional.of(product2));
        when(salesTransactionRepository.save(any(SalesTransaction.class))).thenAnswer(i -> {
            SalesTransaction tx = i.getArgument(0);
            tx.setId(1);
            return tx;
        });
        when(salesItemRepository.save(any(SalesItem.class))).thenAnswer(i -> i.getArgument(0));

        SalesTransaction result = salesTransactionService.createTransaction(cashier, customer, TransactionStatus.PAID, itemRequests);

        assertNotNull(result);
        assertEquals(2, result.getItems().size());
        verify(metricsService, times(1)).incrementTransactionCount();
        verify(productRepository, times(1)).save(product1);
        verify(productRepository, times(1)).save(product2);
    }

    @Test
    void testUpdateTransactionSuccessfully() {
        User cashier = User.builder().id(2).username("jane").build();
        CustomerManagementModel customer = CustomerManagementModel.builder().id(800000000).build();

        ProductManagementModel product1 = ProductManagementModel.builder().id(201).price(50000).quantity(10).build();
        ProductManagementModel product2 = ProductManagementModel.builder().id(202).price(10000).quantity(10).build();

        SalesTransaction existing = SalesTransaction.builder()
                .id(10)
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PENDING)
                .items(new ArrayList<>())
                .build();

        List<SalesItem> updatedItems = List.of(
                SalesItem.builder().product(product1).quantity(1).price(0).build(),
                SalesItem.builder().product(product2).quantity(3).price(0).build()
        );

        when(salesTransactionRepository.findById(10)).thenReturn(Optional.of(existing));
        when(productRepository.findById(201)).thenReturn(Optional.of(product1));
        when(productRepository.findById(202)).thenReturn(Optional.of(product2));
        when(salesTransactionRepository.save(any(SalesTransaction.class))).thenAnswer(i -> i.getArgument(0));

        SalesTransaction result = salesTransactionService.updateTransaction(10, cashier, customer, TransactionStatus.PAID, updatedItems);

        assertNotNull(result);
        assertEquals(TransactionStatus.PAID, result.getStatus());
        assertEquals(2, result.getItems().size());
        verify(productRepository, atLeastOnce()).save(any(ProductManagementModel.class));
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

    // In src/test/java/id/ac/ui/cs/advprog/buildingstore/salesTransaction/service/SalesTransactionServiceTest.java

    @Test
    void testFindByIdReturnsTransaction() {
        SalesTransaction transaction = SalesTransaction.builder().id(42).build();
        when(salesTransactionRepository.findById(42)).thenReturn(Optional.of(transaction));

        Optional<SalesTransaction> result = salesTransactionService.findById(42);

        assertTrue(result.isPresent());
        assertEquals(42, result.get().getId());
    }

    @Test
    void testFindByIdReturnsEmptyWhenNotFound() {
        when(salesTransactionRepository.findById(100)).thenReturn(Optional.empty());

        Optional<SalesTransaction> result = salesTransactionService.findById(100);

        assertFalse(result.isPresent());
    }

    @Test
    void testFindAllReturnsAllTransactions() {
        List<SalesTransaction> transactions = List.of(
                SalesTransaction.builder().id(1).build(),
                SalesTransaction.builder().id(2).build()
        );
        when(salesTransactionRepository.findAll()).thenReturn(transactions);

        Iterable<SalesTransaction> result = salesTransactionService.findAll();

        assertNotNull(result);
        assertEquals(2, ((List<SalesTransaction>) result).size());
    }
}

