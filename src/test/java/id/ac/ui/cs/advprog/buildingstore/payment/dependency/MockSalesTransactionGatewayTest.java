package id.ac.ui.cs.advprog.buildingstore.payment.dependency;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.MockitoAnnotations;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(SpringExtension.class)
class MockSalesTransactionGatewayTest {

    @InjectMocks
    private MockSalesTransactionGateway mockGateway;

    private final ByteArrayOutputStream outputContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;
    private Integer transactionId;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        System.setOut(new PrintStream(outputContent));
        transactionId = 12345;
    }

    @Test
    void exists_WithAnyTransactionId_ShouldReturnTrue() {
        // Act
        boolean result = mockGateway.exists(transactionId);

        // Verify
        assertTrue(result);
        assertTrue(outputContent.toString().contains("[MOCK] Checking if sales transaction " + transactionId + " exists"));
    }

    @Test
    void getStatus_WithAnyTransactionId_ShouldReturnPending() {
        // Act
        String result = mockGateway.getStatus(transactionId);

        // Verify
        assertEquals("PENDING", result);
        assertTrue(outputContent.toString().contains("[MOCK] Getting status for transaction: " + transactionId));
    }

    @Test
    void markAsPaid_WithAnyTransactionId_ShouldLogAction() {
        // Act
        mockGateway.markAsPaid(transactionId);

        // Verify
        assertTrue(outputContent.toString().contains("[MOCK] Marking transaction " + transactionId + " as PAID"));
    }

    @Test
    void markAsPartiallyPaid_WithAnyTransactionId_ShouldLogAction() {
        // Act
        mockGateway.markAsPartiallyPaid(transactionId);

        // Verify
        assertTrue(outputContent.toString().contains("[MOCK] Marking transaction " + transactionId + " as PARTIALLY_PAID"));
    }

    @Test
    void getTotalAmount_WithAnyTransactionId_ShouldReturnHardcodedValue() {
        // Act
        BigDecimal result = mockGateway.getTotalAmount(transactionId);

        // Verify
        assertEquals(0, new BigDecimal("1000000").compareTo(result));
        assertTrue(outputContent.toString().contains("[MOCK] Getting total amount for transaction: " + transactionId));
    }

    @Test
    void exists_WithNegativeTransactionId_ShouldStillReturnTrue() {
        // Arrange
        Integer negativeId = -123;

        // Act
        boolean result = mockGateway.exists(negativeId);

        // Verify
        assertTrue(result);
        assertTrue(outputContent.toString().contains("[MOCK] Checking if sales transaction " + negativeId + " exists"));
    }

    @Test
    void getStatus_WithZeroTransactionId_ShouldReturnPending() {
        // Arrange
        Integer zeroId = 0;

        // Act
        String result = mockGateway.getStatus(zeroId);

        // Verify
        assertEquals("PENDING", result);
        assertTrue(outputContent.toString().contains("[MOCK] Getting status for transaction: " + zeroId));
    }

    @Test
    void getStatus_WithNullTransactionId_ShouldHandleGracefully() {
        // Act & Verify
        assertDoesNotThrow(() -> {
            String result = mockGateway.getStatus(null);
            assertEquals("PENDING", result);
            assertTrue(outputContent.toString().contains("[MOCK] Getting status for transaction: null"));
        });
    }

    @Test
    void getTotalAmount_WithNullTransactionId_ShouldHandleGracefully() {
        // Act
        BigDecimal result = mockGateway.getTotalAmount(null);

        // Verify
        assertEquals(0, new BigDecimal("1000000").compareTo(result));
        assertTrue(outputContent.toString().contains("[MOCK] Getting total amount for transaction: null"));
    }

    @Test
    void markAsPaid_WithNullTransactionId_ShouldHandleGracefully() {
        // Act & Verify
        assertDoesNotThrow(() -> {
            mockGateway.markAsPaid(null);
            assertTrue(outputContent.toString().contains("[MOCK] Marking transaction null as PAID"));
        });
    }

    @Test
    void markAsPartiallyPaid_WithNullTransactionId_ShouldHandleGracefully() {
        // Act & Verify
        assertDoesNotThrow(() -> {
            mockGateway.markAsPartiallyPaid(null);
            assertTrue(outputContent.toString().contains("[MOCK] Marking transaction null as PARTIALLY_PAID"));
        });
    }
}