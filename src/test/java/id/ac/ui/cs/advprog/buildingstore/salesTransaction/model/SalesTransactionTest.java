package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import org.junit.jupiter.api.Test;
import static org.assertj.core.api.Assertions.assertThat;

class SalesTransactionTest {

    @Test
    void testSalesTransactionBuilder() {
        User cashier = User.builder().id(1).username("cashier1").build();

        SalesTransaction transaction = SalesTransaction.builder()
                .id(1)
                .cashier(cashier)
                .customerPhone(123456789)
                .status("In Progress")
                .build();

        assertThat(transaction.getId()).isEqualTo(1);
        assertThat(transaction.getCashier()).isEqualTo(cashier);
        assertThat(transaction.getCustomerPhone()).isEqualTo(123456789);
        assertThat(transaction.getStatus()).isEqualTo("In Progress");
    }

    @Test
    void testSalesTransactionSetters() {
        SalesTransaction transaction = new SalesTransaction();
        User cashier = new User();
        transaction.setId(2);
        transaction.setCashier(cashier);
        transaction.setCustomerPhone(987654321);
        transaction.setStatus("Completed");

        assertThat(transaction.getId()).isEqualTo(2);
        assertThat(transaction.getCashier()).isEqualTo(cashier);
        assertThat(transaction.getCustomerPhone()).isEqualTo(987654321);
        assertThat(transaction.getStatus()).isEqualTo("Completed");
    }
}
