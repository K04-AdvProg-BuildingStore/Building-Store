package id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository;

//import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
//import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
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

  //  @Autowired
   // private ProductManagementRepository productRepository;

    @Autowired
    private SalesTransactionRepository transactionRepository;

    @Test
    void testSaveAndFindById() {
        //ProductManagementModel product = ProductManagementModel.builder()
            //    .name("milk")
            //   .price(15000)
          //      .information("test product")
          //      .build();
      //  product = productRepository.save(product);

        SalesTransaction transaction = SalesTransaction.builder()
                .customerPhone(812345678)
                .status("pending")
                .build();
        transaction = transactionRepository.save(transaction);

        SalesItem item = SalesItem.builder()
                //.product(product)
                .transaction(transaction)
                .quantity(1)
                .price(15000)
                .build();

        SalesItem savedItem = salesItemRepository.save(item);
        SalesItem foundItem = salesItemRepository.findById(String.valueOf(savedItem.getId())).orElse(null);

        assertThat(foundItem).isNotNull();
        assertThat(foundItem.getPrice()).isEqualTo(15000);
        //assertThat(foundItem.getProduct().getName()).isEqualTo("milk");
    }
}
