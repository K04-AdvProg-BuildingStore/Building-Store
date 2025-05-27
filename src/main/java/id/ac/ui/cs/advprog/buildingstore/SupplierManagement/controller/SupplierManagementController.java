package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.controller;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service.SupplierManagementService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/suppliers")
@PreAuthorize("hasRole('ADMIN')")
public class SupplierManagementController {
    private final SupplierManagementService service;

    public SupplierManagementController(SupplierManagementService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<List<SupplierManagementModel>> getAllSuppliers() {
        List<SupplierManagementModel> suppliers = service.getAllSuppliers();
        return ResponseEntity.ok(suppliers);
    }

    @PostMapping
    public ResponseEntity<SupplierManagementModel> addSupplier(@RequestBody SupplierManagementModel supplier) {
        SupplierManagementModel result = service.addSupplier(supplier);
        if (result == null) {
            return ResponseEntity.badRequest().build(); // e.g. phone is null
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/{phoneNumber}")
    public ResponseEntity<SupplierManagementModel> getSupplier(@PathVariable String phoneNumber) {
        Optional<SupplierManagementModel> result = service.getSupplierByPhone(phoneNumber);
        return result.map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{phoneNumber}")
    public ResponseEntity<SupplierManagementModel> updateSupplier(
            @PathVariable String phoneNumber,
            @RequestBody SupplierManagementModel updatedFields) {

        SupplierManagementModel result = service.updateSupplierInfo(
                phoneNumber,
                updatedFields.getName(),
                updatedFields.getAddress(),
                updatedFields.isActive()
        );
        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{phoneNumber}")
    public ResponseEntity<Void> deleteSupplier(@PathVariable String phoneNumber) {
        service.deleteSupplierByPhone(phoneNumber);
        return ResponseEntity.ok().build();
    }
}