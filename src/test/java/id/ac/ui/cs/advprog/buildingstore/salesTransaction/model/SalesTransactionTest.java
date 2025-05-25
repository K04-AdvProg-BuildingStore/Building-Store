package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SalesTransactionTest {

    @Test
    void testSalesTransactionBuilder() {
        User cashier = User.builder().id(1).username("cashier1").build();
        CustomerManagementModel customer = CustomerManagementModel.builder().id(123).build();

        SalesTransaction transaction = SalesTransaction.builder()
                .id(1)
                .cashier(cashier)
                .customer(customer)
                .status(TransactionStatus.PENDING)
                .build();

        assertThat(transaction.getId()).isEqualTo(1);
        assertThat(transaction.getCashier()).isEqualTo(cashier);
        assertThat(transaction.getCustomer()).isEqualTo(customer);
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PENDING);
    }

    @Test
    void testSalesTransactionSetters() {
        SalesTransaction transaction = new SalesTransaction();
        User cashier = new User();
        CustomerManagementModel customer = new CustomerManagementModel();
        transaction.setId(2);
        transaction.setCashier(cashier);
        transaction.setCustomer(customer);
        transaction.setStatus(TransactionStatus.PAID);

        assertThat(transaction.getId()).isEqualTo(2);
        assertThat(transaction.getCashier()).isEqualTo(cashier);
        assertThat(transaction.getCustomer()).isEqualTo(customer);
        assertThat(transaction.getStatus()).isEqualTo(TransactionStatus.PAID);
    }
}
