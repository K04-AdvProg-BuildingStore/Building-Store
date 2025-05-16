package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.
PurchaseHistoryService;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;


@RestController
@RequestMapping("/purchase-history")
@PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")

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

    @GetMapping("/transactions/{phoneNumber}")
    public ResponseEntity<List<PurchaseHistoryViewDTO>> getCustomerPurchases(@PathVariable String phoneNumber) {
        List<PurchaseHistoryViewDTO> purchases = service.getCustomerPurchaseHistory(phoneNumber);
        return ResponseEntity.ok(purchases);
    }

    
}
