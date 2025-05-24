package id.ac.ui.cs.advprog.buildingstore.payment.strategy;

    import id.ac.ui.cs.advprog.buildingstore.payment.dependency.SalesTransactionGateway;
    import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
    import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
    import id.ac.ui.cs.advprog.buildingstore.payment.repository.PaymentRepository;
    import org.junit.jupiter.api.BeforeEach;
    import org.junit.jupiter.api.Test;
    import org.mockito.InjectMocks;
    import org.mockito.Mock;
    import org.mockito.MockitoAnnotations;

    import java.math.BigDecimal;
    import java.util.ArrayList;
    import java.util.List;
    import java.util.UUID;

    import static org.junit.jupiter.api.Assertions.*;
    import static org.mockito.ArgumentMatchers.anyInt;
    import static org.mockito.Mockito.*;

    class PaymentStrategyTest {

        @Mock
        private SalesTransactionGateway salesTransactionGateway;

        @Mock
        private PaymentRepository paymentRepository;

        @InjectMocks
        private FullPaymentStrategy fullPaymentStrategy;

        @InjectMocks
        private InstallmentPaymentStrategy installmentPaymentStrategy;

        private Payment fullPayment;
        private Payment installmentPayment;
        private Integer transactionId;
        private UUID testId;

        @BeforeEach
        void setUp() {
            MockitoAnnotations.openMocks(this);

            testId = UUID.randomUUID();
            transactionId = 12345;
            fullPayment = Payment.builder()
                    .id(testId)
                    .amount(BigDecimal.valueOf(100000))
                    .status(PaymentStatus.FULL)
                    .method("Credit Card")
                    .salesTransactionId(transactionId)
                    .build();

            installmentPayment = Payment.builder()
                    .id(testId)
                    .amount(BigDecimal.valueOf(50000))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Debit Card")
                    .salesTransactionId(transactionId)
                    .build();
        }

        // FullPaymentStrategy Tests

        @Test
        void fullPaymentSupports_WithPaidStatus_ShouldReturnTrue() {
            // Verify
            assertTrue(fullPaymentStrategy.supports(PaymentStatus.FULL));
        }

        @Test
        void fullPaymentSupports_WithInstallmentStatus_ShouldReturnFalse() {
            // Verify
            assertFalse(fullPaymentStrategy.supports(PaymentStatus.INSTALLMENT));
        }

        @Test
        void fullPaymentExecute_WithValidTransaction_ShouldMarkAsPaid() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PENDING");
            doNothing().when(salesTransactionGateway).markAsPaid(transactionId);

            // Act
            Payment result = fullPaymentStrategy.execute(fullPayment);

            // Verify
            assertNotNull(result);
            assertEquals(fullPayment, result);
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway).getStatus(transactionId);
            verify(salesTransactionGateway).markAsPaid(transactionId);
        }

        @Test
        void fullPaymentExecute_WithNonExistingTransaction_ShouldThrowException() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(false);

            // Act & Verify
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> fullPaymentStrategy.execute(fullPayment));

            assertEquals("Sales transaction not found.", exception.getMessage());
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway, never()).markAsPaid(anyInt());
        }

        @Test
        void fullPaymentExecute_WithAlreadyPaidTransaction_ShouldThrowException() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("FULL");

            // Act & Verify
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> fullPaymentStrategy.execute(fullPayment));

            assertEquals("This transaction is already paid in full.", exception.getMessage());
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway).getStatus(transactionId);
            verify(salesTransactionGateway, never()).markAsPaid(anyInt());
        }

        @Test
        void fullPaymentAfterInstallment_ShouldThrowException() {
            // Arrange
            // Mock a transaction that is already in PARTIALLY_PAID state
            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PARTIALLY_PAID");

            // Act & Verify
            // Attempting full payment on a transaction with partial payments should fail
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> fullPaymentStrategy.execute(fullPayment));

            assertEquals("This transaction is already partially paid. Please continue with installment payments.",
                    exception.getMessage());
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway).getStatus(transactionId);
            verify(salesTransactionGateway, never()).markAsPaid(anyInt());
        }

        // InstallmentPaymentStrategy Tests

        @Test
        void installmentPaymentSupports_WithInstallmentStatus_ShouldReturnTrue() {
            // Verify
            assertTrue(installmentPaymentStrategy.supports(PaymentStatus.INSTALLMENT));
        }

        @Test
        void installmentPaymentSupports_WithPaidStatus_ShouldReturnFalse() {
            // Verify
            assertFalse(installmentPaymentStrategy.supports(PaymentStatus.FULL));
        }

        @Test
        void installmentPaymentExecute_WithFirstPayment_ShouldMarkAsPartiallyPaid() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PENDING");
            when(salesTransactionGateway.getTotalAmount(transactionId)).thenReturn(new BigDecimal("100000"));
            when(paymentRepository.findAllBySalesTransactionId(transactionId)).thenReturn(new ArrayList<>());
            doNothing().when(salesTransactionGateway).markAsPartiallyPaid(transactionId);

            // Act
            Payment result = installmentPaymentStrategy.execute(installmentPayment);

            // Verify
            assertNotNull(result);
            assertEquals(installmentPayment, result);
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway).getStatus(transactionId);
            verify(salesTransactionGateway).getTotalAmount(transactionId);
            verify(paymentRepository).findAllBySalesTransactionId(transactionId);
            verify(salesTransactionGateway).markAsPartiallyPaid(transactionId);
            verify(salesTransactionGateway, never()).markAsPaid(anyInt());
        }

        @Test
        void installmentPaymentExecute_WithFinalPayment_ShouldMarkAsPaid() {
            // Arrange
            Payment previousPayment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(new BigDecimal("50000"))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Cash")
                    .salesTransactionId(transactionId)
                    .build();

            List<Payment> previousPayments = new ArrayList<>();
            previousPayments.add(previousPayment);

            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PARTIALLY_PAID");
            when(salesTransactionGateway.getTotalAmount(transactionId)).thenReturn(new BigDecimal("100000"));
            when(paymentRepository.findAllBySalesTransactionId(transactionId)).thenReturn(previousPayments);
            doNothing().when(salesTransactionGateway).markAsPaid(transactionId);

            // Act
            Payment result = installmentPaymentStrategy.execute(installmentPayment);

            // Verify
            assertNotNull(result);
            assertEquals(installmentPayment, result);
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway).getStatus(transactionId);
            verify(salesTransactionGateway).getTotalAmount(transactionId);
            verify(paymentRepository).findAllBySalesTransactionId(transactionId);
            verify(salesTransactionGateway).markAsPaid(transactionId);
            verify(salesTransactionGateway, never()).markAsPartiallyPaid(anyInt());
        }

        @Test
        void installmentPaymentExecute_WithNonExistingTransaction_ShouldThrowException() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(false);

            // Act & Verify
            IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                    () -> installmentPaymentStrategy.execute(installmentPayment));

            assertEquals("Sales transaction not found.", exception.getMessage());
            verify(salesTransactionGateway).exists(transactionId);
            verify(paymentRepository, never()).findAllBySalesTransactionId(anyInt());
        }

        @Test
        void installmentPaymentExecute_WithAlreadyPaidTransaction_ShouldThrowException() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("FULL");

            // Act & Verify
            IllegalStateException exception = assertThrows(IllegalStateException.class,
                    () -> installmentPaymentStrategy.execute(installmentPayment));

            assertEquals("This transaction is already paid in full.", exception.getMessage());
            verify(salesTransactionGateway).exists(transactionId);
            verify(salesTransactionGateway).getStatus(transactionId);
            verify(paymentRepository, never()).findAllBySalesTransactionId(anyInt());
        }

        @Test
        void installmentPaymentExecute_WithOverpayment_ShouldMarkAsPaid() {
            // Arrange
            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PENDING");
            when(salesTransactionGateway.getTotalAmount(transactionId)).thenReturn(new BigDecimal("40000"));
            when(paymentRepository.findAllBySalesTransactionId(transactionId)).thenReturn(new ArrayList<>());
            doNothing().when(salesTransactionGateway).markAsPaid(transactionId);

            // Act
            Payment result = installmentPaymentStrategy.execute(installmentPayment);

            // Verify
            assertNotNull(result);
            assertEquals(installmentPayment, result);
            verify(salesTransactionGateway).markAsPaid(transactionId);
            verify(salesTransactionGateway, never()).markAsPartiallyPaid(anyInt());
        }

        @Test
        void installmentPaymentExecute_WithMultiplePayments_ShouldTrackProgress() {
            // Arrange
            BigDecimal totalAmount = new BigDecimal("100000");
            Payment firstPayment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(new BigDecimal("30000"))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Cash")
                    .salesTransactionId(transactionId)
                    .build();

            List<Payment> previousPayments = new ArrayList<>();
            previousPayments.add(firstPayment);

            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PARTIALLY_PAID");
            when(salesTransactionGateway.getTotalAmount(transactionId)).thenReturn(totalAmount);
            when(paymentRepository.findAllBySalesTransactionId(transactionId)).thenReturn(previousPayments);
            doNothing().when(salesTransactionGateway).markAsPartiallyPaid(transactionId);

            // Second payment that still doesn't complete the transaction
            Payment secondPayment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(new BigDecimal("20000"))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Debit Card")
                    .salesTransactionId(transactionId)
                    .build();

            // Act
            Payment result = installmentPaymentStrategy.execute(secondPayment);

            // Verify
            assertNotNull(result);
            assertEquals(secondPayment, result);
            verify(salesTransactionGateway).markAsPartiallyPaid(transactionId);
            verify(salesTransactionGateway, never()).markAsPaid(anyInt());
        }

        @Test
        void completingInstallmentsWithMultiplePayments_ShouldEventuallyMarkAsPaid() {
            // Arrange
            BigDecimal totalAmount = new BigDecimal("100000");

            // First previous payment
            Payment firstPayment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(new BigDecimal("30000"))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Cash")
                    .salesTransactionId(transactionId)
                    .build();

            // Second previous payment
            Payment secondPayment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(new BigDecimal("40000"))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Debit Card")
                    .salesTransactionId(transactionId)
                    .build();

            List<Payment> previousPayments = new ArrayList<>();
            previousPayments.add(firstPayment);
            previousPayments.add(secondPayment);

            when(salesTransactionGateway.exists(transactionId)).thenReturn(true);
            when(salesTransactionGateway.getStatus(transactionId)).thenReturn("PARTIALLY_PAID");
            when(salesTransactionGateway.getTotalAmount(transactionId)).thenReturn(totalAmount);
            when(paymentRepository.findAllBySalesTransactionId(transactionId)).thenReturn(previousPayments);
            doNothing().when(salesTransactionGateway).markAsPaid(transactionId);

            // Final payment that completes the transaction
            Payment finalPayment = Payment.builder()
                    .id(UUID.randomUUID())
                    .amount(new BigDecimal("30000"))
                    .status(PaymentStatus.INSTALLMENT)
                    .method("Credit Card")
                    .salesTransactionId(transactionId)
                    .build();

            // Act
            Payment result = installmentPaymentStrategy.execute(finalPayment);

            // Verify
            assertNotNull(result);
            assertEquals(finalPayment, result);
            verify(salesTransactionGateway).markAsPaid(transactionId);
            verify(salesTransactionGateway, never()).markAsPartiallyPaid(anyInt());
        }
    }