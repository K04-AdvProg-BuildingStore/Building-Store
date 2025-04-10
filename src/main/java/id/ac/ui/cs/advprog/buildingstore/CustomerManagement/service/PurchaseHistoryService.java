package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.PurchaseHistoryRepository;
import java.util.List;

public class PurchaseHistoryService {
    private final PurchaseHistoryRepository respository;

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
    
}
