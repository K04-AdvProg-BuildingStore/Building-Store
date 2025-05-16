package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.CustomPurchaseHistoryRepository;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import java.util.List;

@Service
public class PurchaseHistoryService {
    private final PurchaseHistoryRepository respository;

        @Autowired
    private CustomPurchaseHistoryRepository customRepo;

    public PurchaseHistoryService(PurchaseHistoryRepository repository){
        this.respository = repository;
    }

    public PurchaseHistoryModel addPurchase(PurchaseHistoryModel purchase) {
        String phone = purchase.getPhoneNumber();
        if (phone == null || phone.isBlank()){
            return null;
        }
        return respository.save(purchase);
    }
    public List<PurchaseHistoryModel> getPurchaseHistory(String phoneNumber){
        return respository.findByPhoneNumber(phoneNumber);
    }

    public List<PurchaseHistoryViewDTO> getCustomerPurchases(String phoneNumber) {
        return getCustomerPurchaseHistory(phoneNumber);
    }

    public List<PurchaseHistoryViewDTO> getCustomerPurchaseHistory(String phoneNumber) {
        return customRepo.findPurchaseHistoryByPhoneNumber(phoneNumber);
    }
    
}
