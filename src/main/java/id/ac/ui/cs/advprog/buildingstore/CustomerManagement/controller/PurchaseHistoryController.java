package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import java.util.List;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.http.HttpStatus;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.PurchaseHistoryService;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@RestController
@RequestMapping("/api/purchase-history") // Changed path to match common REST conventions
@PreAuthorize("isAuthenticated()") // Simplified security rule - require only authentication
public class PurchaseHistoryController {
    private static final Logger logger = LoggerFactory.getLogger(PurchaseHistoryController.class);
    private final PurchaseHistoryService service;

    public PurchaseHistoryController(PurchaseHistoryService service) {
        this.service = service;
    }
    
    // Use only customer ID for retrieving purchase history
    @GetMapping("/{customerId}")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN', 'USER')")
    public ResponseEntity<List<PurchaseHistoryViewDTO>> getPurchaseHistory(@PathVariable Integer customerId) {
        logger.info("Received request for purchase history of customer ID: {}", customerId);
        try {
            List<PurchaseHistoryViewDTO> history = service.getCustomerPurchaseHistoryById(customerId);
            logger.info("Returning {} purchase history records", history.size());
            return ResponseEntity.ok(history);
        } catch (Exception e) {
            logger.error("Error retrieving purchase history: {}", e.getMessage(), e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
}