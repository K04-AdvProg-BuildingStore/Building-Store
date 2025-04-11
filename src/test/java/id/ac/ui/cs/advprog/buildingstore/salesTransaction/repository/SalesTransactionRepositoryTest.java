package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SalesTransactionRepositoryTest {

    @Autowired
    private SalesTransactionRepository transactionRepository;

    @Test
    void testSaveAndFindById() {
        // Create and save a transaction
        SalesTransaction transaction = SalesTransaction.builder()
                .customerPhone(812345678)
                .status("pending")
                .build();

        SalesTransaction saved = transactionRepository.save(transaction);

        // Retrieve it
        SalesTransaction found = transactionRepository.findById(saved.getId()).orElse(null);

        // Assertions
        assertThat(found).isNotNull();
        assertThat(found.getCustomerPhone()).isEqualTo(812345678);
        assertThat(found.getStatus()).isEqualTo("pending");
    }
}
