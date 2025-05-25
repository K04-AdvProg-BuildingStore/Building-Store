package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.CustomerManagementRepository;
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

    @Autowired
    private ProductManagementRepository productRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private CustomerManagementRepository customerRepository;

    @Test
    void testSaveAndFindById() {
        User cashier = User.builder().username("cashier1").build();
        cashier = userRepository.save(cashier);
        CustomerManagementModel customer = CustomerManagementModel.builder().phoneNumber("08123456789").build();
        customer = customerRepository.save(customer);

        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PENDING)
                .build();
        transaction = transactionRepository.save(transaction);

        ProductManagementModel product = ProductManagementModel.builder()
                .name("Test Product")
                .quantity(10)
                .price(15000)
                .status("Available")
                .build();
        product = productRepository.save(product);

        SalesItem item = SalesItem.builder()
                .transaction(transaction)
                .product(product)
                .quantity(1)
                .price(15000)
                .build();

        SalesItem savedItem = salesItemRepository.save(item);
        SalesItem foundItem = salesItemRepository.findById(savedItem.getId()).orElse(null);

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getPrice()).isEqualTo(15000);
        assertThat(foundItem.getTransaction().getId()).isEqualTo(transaction.getId());
    }
}

