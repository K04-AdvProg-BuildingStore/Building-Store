package id.ac.ui.cs.advprog.buildingstore.payment.dependency;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.service.SalesTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

class SalesTransactionGatewayImplTest {

    @Mock
    private SalesTransactionService salesTransactionService;

    @InjectMocks
    private SalesTransactionGatewayImpl salesTransactionGateway;

    private SalesTransaction pendingTransaction;
    private SalesTransaction paidTransaction;
    private List<SalesItem> items;
    private Integer transactionId;
    private User cashier;
    private CustomerManagementModel customer;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        transactionId = 12345;
        cashier = new User();
        customer = new CustomerManagementModel();
        items = new ArrayList<>();

        // Create first item
        ProductManagementModel product1 = new ProductManagementModel();
        SalesItem item1 = SalesItem.builder()
                .product(product1)
                .quantity(2)
                .price(50000)
                .build();

        // Create second item
        ProductManagementModel product2 = new ProductManagementModel();
        SalesItem item2 = SalesItem.builder()
                .product(product2)
                .quantity(1)
                .price(100000)
                .build();

        items.add(item1);
        items.add(item2);

        pendingTransaction = SalesTransaction.builder()
                .id(transactionId)
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PENDING)
                .items(items)
                .build();

        paidTransaction = SalesTransaction.builder()
                .id(transactionId)
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PAID)
                .items(items)
                .build();

        // Set transaction reference in items
        item1.setTransaction(pendingTransaction);
        item2.setTransaction(pendingTransaction);
    }

    @Test
    void testExists_WhenTransactionExists_ReturnsTrue() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.of(pendingTransaction));

        boolean result = salesTransactionGateway.exists(transactionId);

        assertTrue(result);
        verify(salesTransactionService).findById(transactionId);
    }

    @Test
    void testExists_WhenTransactionDoesNotExist_ReturnsFalse() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.empty());

        boolean result = salesTransactionGateway.exists(transactionId);

        assertFalse(result);
        verify(salesTransactionService).findById(transactionId);
    }

    @Test
    void testGetStatus_WhenTransactionExists_ReturnsStatus() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.of(pendingTransaction));

        String result = salesTransactionGateway.getStatus(transactionId);

        assertEquals("PENDING", result);
        verify(salesTransactionService).findById(transactionId);
    }

    @Test
    void testGetStatus_WhenTransactionDoesNotExist_ReturnsNull() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.empty());

        String result = salesTransactionGateway.getStatus(transactionId);

        assertNull(result);
        verify(salesTransactionService).findById(transactionId);
    }

    @Test
    void testGetTotalAmount_WhenTransactionExists_ReturnsCorrectTotal() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.of(pendingTransaction));

        BigDecimal result = salesTransactionGateway.getTotalAmount(transactionId);

        // 2*50000 + 1*100000 = 200000
        assertEquals(BigDecimal.valueOf(200000), result);
        verify(salesTransactionService).findById(transactionId);
    }

    @Test
    void testGetTotalAmount_WhenTransactionDoesNotExist_ReturnsZero() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.empty());

        BigDecimal result = salesTransactionGateway.getTotalAmount(transactionId);

        assertEquals(BigDecimal.ZERO, result);
        verify(salesTransactionService).findById(transactionId);
    }

    @Test
    void testMarkAsPaid_WhenTransactionExists_UpdatesStatus() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.of(pendingTransaction));
        when(salesTransactionService.updateTransaction(
                eq(transactionId),
                eq(cashier),
                eq(customer),
                eq(TransactionStatus.PAID),
                eq(items)
        )).thenReturn(paidTransaction);

        salesTransactionGateway.markAsPaid(transactionId);

        verify(salesTransactionService).findById(transactionId);
        verify(salesTransactionService).updateTransaction(
                eq(transactionId),
                eq(cashier),
                eq(customer),
                eq(TransactionStatus.PAID),
                eq(items)
        );
    }

    @Test
    void testMarkAsPaid_WhenTransactionDoesNotExist_ThrowsException() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            salesTransactionGateway.markAsPaid(transactionId);
        });

        verify(salesTransactionService).findById(transactionId);
        verify(salesTransactionService, never()).updateTransaction(any(), any(), any(), any(), any());
    }

    @Test
    void testMarkAsPartiallyPaid_WhenTransactionDoesNotExist_ThrowsException() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.empty());

        assertThrows(IllegalArgumentException.class, () -> {
            salesTransactionGateway.markAsPartiallyPaid(transactionId);
        });

        verify(salesTransactionService).findById(transactionId);
        verify(salesTransactionService, never()).updateTransaction(any(), any(), any(), any(), any());
    }

    @Test
    void testUpdateTransactionStatus_WhenStatusIsAlreadySet_DoesNotCallUpdate() {
        when(salesTransactionService.findById(transactionId)).thenReturn(Optional.of(paidTransaction));

        // Try to mark a PAID transaction as PAID again
        salesTransactionGateway.markAsPaid(transactionId);

        verify(salesTransactionService).findById(transactionId);
        // Verify updateTransaction was not called since status is already PAID
        verify(salesTransactionService, never()).updateTransaction(any(), any(), any(), any(), any());
    }
}
