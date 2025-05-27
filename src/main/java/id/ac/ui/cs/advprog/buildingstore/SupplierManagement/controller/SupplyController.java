package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.controller;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service.SupplyService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.access.prepost.PreAuthorize;

import java.util.List;

@RestController
@RequestMapping("/api/supplies")
@PreAuthorize("hasRole('ADMIN')")
public class SupplyController {

    @Autowired
    private SupplyService service;

    @GetMapping
    public List<SupplyModel> listAll() {
        return service.getAllSupplies();
    }

    @GetMapping("/{id}")
    public SupplyModel getOne(@PathVariable Integer id) {
        return service.getSupplyById(id);
    }

    @GetMapping("/by-supplier-phone/{phone}")
    public List<SupplyModel> bySupplier(@PathVariable String phone) {
        return service.getSuppliesBySupplierPhone(phone);
    }

    @GetMapping("/by-product/{productId}")
    public List<SupplyModel> byProduct(@PathVariable Integer productId) {
        return service.getSuppliesByProduct(productId);
    }

    @PostMapping
    public SupplyModel create(@RequestBody SupplyModel supply) {
        return service.createSupply(supply);
    }

    @PutMapping("/{id}")
    public SupplyModel update(@PathVariable Integer id,
                              @RequestBody SupplyModel supply) {
        return service.updateSupply(id, supply);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable Integer id) {
        service.deleteSupply(id);
        return ResponseEntity.noContent().build();
    }
}