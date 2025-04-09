package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Date;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
public class PurchaseHistoryRepositoryTest {

    @Autowired
    private PurchaseHistoryRepository repository;

    @Test
    void testFindByPhoneNumber() {
        PurchaseHistoryModel history1 = PurchaseHistoryModel.builder()
                .phoneNumber("0888123456")
                .itemName("Printer")
                .quantity(1)
                .totalAmount(1200000.0)
                .purchaseDate(new Date())
                .build();

        PurchaseHistoryModel history2 = PurchaseHistoryModel.builder()
                .phoneNumber("0888123456")
                .itemName("Paper")
                .quantity(10)
                .totalAmount(50000.0)
                .purchaseDate(new Date())
                .build();

        repository.saveAll(List.of(history1, history2));

        List<PurchaseHistoryModel> result = repository.findByPhoneNumber("0888123456");

        assertEquals(2, result.size());
        assertTrue(result.stream().anyMatch(h -> h.getItemName().equals("Printer")));
        assertTrue(result.stream().anyMatch(h -> h.getItemName().equals("Paper")));
    }

    @Test
    void testFindByPhoneNumberReturnsEmptyIfNotFound() {
        List<PurchaseHistoryModel> result = repository.findByPhoneNumber("0000000000");
        assertTrue(result.isEmpty());
    }
}
