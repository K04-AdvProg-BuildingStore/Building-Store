package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.PurchaseHistoryService;

@RestController
@RequestMapping("/purchase-history")
public class PurchaseHistoryController {

    private final PurchaseHistoryService service;

    public PurchaseHistoryController(PurchaseHistoryService service) {
        this.service = service;
    }

    @PostMapping
    public ResponseEntity<PurchaseHistoryModel> addPurchase(@RequestBody PurchaseHistoryModel purchase) {
        PurchaseHistoryModel result = service.addPurchase(purchase);
        if (result == null) {
            return ResponseEntity.badRequest().build(); // Phone number is required
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<List<PurchaseHistoryModel>> getPurchaseHistory(@PathVariable String phoneNumber) {
        List<PurchaseHistoryModel> history = service.getPurchaseHistory(phoneNumber);
        return ResponseEntity.ok(history);
    }

    
}
