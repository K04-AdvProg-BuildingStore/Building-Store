package id.ac.ui.cs.advprog.buildingstore.salesTransaction.service;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesItem;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesItemRepository;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
@RequiredArgsConstructor
public class SalesItemService {

    private final SalesItemRepository salesItemRepository;

    public Optional<SalesItem> findById(String id) {
        return salesItemRepository.findById(id);
    }

    public SalesItem createSalesItem(
            ProductManagementModel product,
            SalesTransaction transaction,
            int quantity,
            int price
    ) {
        SalesItem newItem = SalesItem.builder()
                .product(product)
                .quantity(quantity)
                .price(price)
                .transaction(transaction)
                .build();

        return salesItemRepository.save(newItem);
    }

    public Iterable<SalesItem> findAll() {
        return salesItemRepository.findAll();
    }

    public Optional<SalesItem> getSalesItemById(String id) {
        return salesItemRepository.findById(id);
    }
}
