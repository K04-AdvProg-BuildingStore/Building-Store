package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTOImpl;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import java.util.List;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository repository;

    public PurchaseHistoryService(PurchaseHistoryRepository repository){
        this.repository = repository;
    }

    public PurchaseHistoryModel addPurchase(PurchaseHistoryModel purchase) {
        if (purchase == null) {
            return null;
        }
        
        String phone = purchase.getPhoneNumber();
        if (phone == null || phone.isBlank()) {
            return null;
        }
        
        // Add validation for negative quantities
        if (purchase.getQuantity() < 0 || purchase.getTotalAmount() < 0) {
            return null;
        }
        
        return repository.save(purchase);
    }
    
    public List<PurchaseHistoryViewDTO> getCustomerPurchaseHistoryById(Integer customerId) {
        if (customerId == null) {
            return new ArrayList<>();
        }
        
        List<Object[]> rawList = repository.findFullCustomerPurchaseHistoryByIdRaw(customerId);
        return rawList.stream()
            .map(row -> new PurchaseHistoryViewDTOImpl(
                row[0] != null ? ((Number) row[0]).intValue() : null, // customerId
                (String) row[1],
                (String) row[2],
                row[3] != null ? ((Number) row[3]).intValue() : null, // transactionId
                row[4] != null ? ((Number) row[4]).intValue() : null, // productId
                row[5] != null ? ((Number) row[5]).intValue() : null, // quantity
                row[6] != null ? ((Number) row[6]).doubleValue() : null // price
            ))
            .collect(Collectors.toList());
    }
}