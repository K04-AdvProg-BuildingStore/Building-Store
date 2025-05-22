package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
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
        User cashier = User.builder().id(1).username("cashier1").build();
        CustomerManagementModel customer = CustomerManagementModel.builder().id(123).build();

        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PENDING)
                .build();

        SalesTransaction saved = transactionRepository.save(transaction);

        // Retrieve it
        SalesTransaction found = transactionRepository.findById(saved.getId()).orElse(null);

        // Assertions
        assertThat(found).isNotNull();
        assertThat(found.getCustomer().getId()).isEqualTo(123);
        assertThat(found.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }
}
