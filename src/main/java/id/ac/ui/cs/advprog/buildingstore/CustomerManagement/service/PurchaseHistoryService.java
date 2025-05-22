package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTOImpl;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository repository;

    public PurchaseHistoryService(PurchaseHistoryRepository repository){
        this.repository = repository;
    }

    public PurchaseHistoryModel addPurchase(PurchaseHistoryModel purchase) {
        String phone = purchase.getPhoneNumber();
        if (phone == null || phone.isBlank()){
            return null;
        }
        return repository.save(purchase);
    }

    public List<PurchaseHistoryViewDTO> getCustomerPurchaseHistory(String phoneNumber) {
        List<Object[]> rawList = repository.findFullCustomerPurchaseHistoryRaw(phoneNumber);
        return rawList.stream()
            .map(row -> new PurchaseHistoryViewDTOImpl(
                (String) row[0],
                (String) row[1],
                row[2] != null ? ((Number) row[2]).intValue() : null,
                row[3] != null ? ((Number) row[3]).intValue() : null, // productId
                row[4] != null ? ((Number) row[4]).intValue() : null, // quantity
                row[5] != null ? ((Number) row[5]).doubleValue() : null // price
            ))
            .collect(Collectors.toList());
    }
}