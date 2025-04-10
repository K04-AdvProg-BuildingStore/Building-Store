package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.assertThat;

class SalesItemTest {

    @Test
    void testSalesItemBuilder() {
        ProductManagementModel product = ProductManagementModel.builder()
                .id(1)
                .name("Product A")
                .price(10000)
                .build();

        SalesTransaction transaction = SalesTransaction.builder().id(1).build();

        SalesItem item = SalesItem.builder()
                .id("abc-123")
                .product(product)
                .quantity(2)
                .price(20000)
                .transaction(transaction)
                .build();

        assertThat(item.getId()).isEqualTo("abc-123");
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(2);
        assertThat(item.getPrice()).isEqualTo(20000);
        assertThat(item.getTransaction()).isEqualTo(transaction);
    }

    @Test
    void testSalesItemSetters() {
        SalesItem item = new SalesItem();
        ProductManagementModel product = new ProductManagementModel();
        SalesTransaction transaction = new SalesTransaction();

        item.setId("xyz-999");
        item.setProduct(product);
        item.setQuantity(5);
        item.setPrice(50000);
        item.setTransaction(transaction);

        assertThat(item.getId()).isEqualTo("xyz-999");
        assertThat(item.getProduct()).isEqualTo(product);
        assertThat(item.getQuantity()).isEqualTo(5);
        assertThat(item.getPrice()).isEqualTo(50000);
        assertThat(item.getTransaction()).isEqualTo(transaction);
    }
}
