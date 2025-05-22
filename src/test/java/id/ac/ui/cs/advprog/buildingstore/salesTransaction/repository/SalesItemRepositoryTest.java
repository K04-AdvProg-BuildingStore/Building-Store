package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
public class SalesItemRepositoryTest {

    @Autowired
    private SalesItemRepository salesItemRepository;

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
        transaction = transactionRepository.save(transaction);

        SalesItem item = SalesItem.builder()
                .transaction(transaction)
                .quantity(1)
                .price(15000)
                .build();

        SalesItem savedItem = salesItemRepository.save(item);
        SalesItem foundItem = salesItemRepository.findById(String.valueOf(savedItem.getId())).orElse(null);

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getPrice()).isEqualTo(15000);
        assertThat(foundItem.getTransaction().getId()).isEqualTo(transaction.getId());
    }
}

