package id.ac.ui.cs.advprog.buildingstore.payment.repository;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import id.ac.ui.cs.advprog.buildingstore.payment.model.Payment;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.EmptyResultDataAccessException;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
class PaymentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private PaymentRepository paymentRepository;

    // Happy Path Tests
    
    @Test
    void save_WithValidPayment_ShouldPersistPayment() {
        // Create a payment
        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.PAID)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        // Save the payment
        Payment savedPayment = paymentRepository.save(payment);
        
        // Flush changes to DB
        entityManager.flush();
        entityManager.clear();
        
        // Verify
        assertNotNull(savedPayment.getId());
        
        Optional<Payment> foundPayment = paymentRepository.findById(savedPayment.getId());
        assertTrue(foundPayment.isPresent());
        assertTrue(payment.getAmount().compareTo(foundPayment.get().getAmount()) == 0);
        assertEquals(payment.getStatus(), foundPayment.get().getStatus());
        assertEquals(payment.getMethod(), foundPayment.get().getMethod());
        assertEquals(payment.getSalesTransactionId(), foundPayment.get().getSalesTransactionId());
    }

    @Test
    void findById_WithExistingPayment_ShouldReturnPayment() {
        // Create a payment
        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.PAID)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        // Persist the entity
        Payment persistedPayment = entityManager.persistAndFlush(payment);
        entityManager.clear();

        // Find by ID
        Optional<Payment> foundPayment = paymentRepository.findById(persistedPayment.getId());
        
        // Verify
        assertTrue(foundPayment.isPresent());
        assertTrue(payment.getAmount().compareTo(foundPayment.get().getAmount()) == 0);
    }

    @Test
    void findAll_WithExistingPayments_ShouldReturnAllPayments() {
        // Clear any existing data to ensure clean test
        paymentRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
        
        // Create payments
        Payment payment1 = Payment.builder()
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.PAID)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        Payment payment2 = Payment.builder()
                .amount(BigDecimal.valueOf(200000))
                .status(PaymentStatus.INSTALLMENT)
                .method("Debit Card")
                .salesTransactionId(67890)
                .build();

        // Persist the entities
        entityManager.persist(payment1);
        entityManager.persist(payment2);
        entityManager.flush();
        entityManager.clear();

        // Find all
        List<Payment> payments = paymentRepository.findAll();
        
        // Verify
        assertEquals(2, payments.size());
    }

    @Test
    void deleteById_WithExistingPayment_ShouldRemovePayment() {
        // Create a payment
        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.PAID)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        // Persist the entity
        Payment persistedPayment = entityManager.persistAndFlush(payment);
        entityManager.clear();
        
        // Delete the payment
        paymentRepository.deleteById(persistedPayment.getId());
        entityManager.flush();
        entityManager.clear();
        
        // Verify deletion
        Optional<Payment> deletedPayment = paymentRepository.findById(persistedPayment.getId());
        assertFalse(deletedPayment.isPresent());
    }
    
    @Test
    void update_WithExistingPayment_ShouldUpdatePayment() {
        // Create a payment
        Payment payment = Payment.builder()
                .amount(BigDecimal.valueOf(100000))
                .status(PaymentStatus.PAID)
                .method("Credit Card")
                .salesTransactionId(12345)
                .build();

        // Persist the entity
        Payment persistedPayment = entityManager.persistAndFlush(payment);
        entityManager.clear();
        
        // Update the payment
        persistedPayment.setAmount(BigDecimal.valueOf(200000));
        persistedPayment.setStatus(PaymentStatus.INSTALLMENT);
        paymentRepository.save(persistedPayment);
        entityManager.flush();
        entityManager.clear();
        
        // Verify update
        Optional<Payment> updatedPayment = paymentRepository.findById(persistedPayment.getId());
        assertTrue(updatedPayment.isPresent());
        assertTrue(BigDecimal.valueOf(200000).compareTo(updatedPayment.get().getAmount()) == 0);
        assertEquals(PaymentStatus.INSTALLMENT, updatedPayment.get().getStatus());
    }
    
    // Unhappy Path Tests

    @Test
    void findById_WithNonExistingId_ShouldReturnEmpty() {
        // Generate a random UUID
        UUID randomId = UUID.randomUUID();

        // Find by non-existent ID
        Optional<Payment> foundPayment = paymentRepository.findById(randomId);
        
        // Verify
        assertFalse(foundPayment.isPresent());
    }
    
    @Test
    void deleteById_WithNonExistingId_ShouldNotThrowException() {
        // In JpaRepository implementation, deleteById for a non-existent ID is a no-op
        // and doesn't throw an exception
        UUID randomId = UUID.randomUUID();
        
        // This should NOT throw an exception
        assertDoesNotThrow(() -> {
            paymentRepository.deleteById(randomId);
            entityManager.flush();
        });
        
        // We can verify the operation was harmless by checking that no records were affected
        // (this is optional but helps verify the test is meaningful)
        long countBefore = paymentRepository.count();
        paymentRepository.deleteById(randomId); // Delete non-existent ID
        entityManager.flush();
        long countAfter = paymentRepository.count();
        
        assertEquals(countBefore, countAfter, "Deleting non-existent ID should not change record count");
    }
    
    @Test
    void findAll_WithNoPayments_ShouldReturnEmptyList() {
        // Clear any existing data
        paymentRepository.deleteAll();
        entityManager.flush();
        entityManager.clear();
        
        // Find all
        List<Payment> payments = paymentRepository.findAll();
        
        // Verify
        assertTrue(payments.isEmpty());
    }
}
