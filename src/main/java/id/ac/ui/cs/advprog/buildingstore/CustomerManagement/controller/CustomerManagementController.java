package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import java.util.List;
import java.util.Optional;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.security.access.prepost.PreAuthorize;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.CustomerManagementService;

@RestController
@RequestMapping("/customers")
@PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")

public class CustomerManagementController {

    private final CustomerManagementService service;

    public CustomerManagementController(CustomerManagementService service) {
        this.service = service;
    }

    @PostMapping

    public ResponseEntity<CustomerManagementModel> addCustomer(@RequestBody CustomerManagementModel customer) {
        CustomerManagementModel result = service.addCustomer(customer);
        if (result == null) {
            return ResponseEntity.badRequest().build(); // e.g. phone is null
        }
        return ResponseEntity.ok(result);
    }

    @GetMapping("/all")
    public ResponseEntity<List<CustomerManagementModel>> getAllCustomers() {
        List<CustomerManagementModel> customers = service.getAllCustomers();
        return ResponseEntity.ok(customers);
    }

    @GetMapping("/{phoneNumber}")
   
    public ResponseEntity<CustomerManagementModel> getCustomer(@PathVariable String phoneNumber) {
        Optional<CustomerManagementModel> result = service.getCustomerByPhone(phoneNumber);
        return result.map(ResponseEntity::ok)
                     .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{phoneNumber}")
    public ResponseEntity<CustomerManagementModel> updateCustomer(
            @PathVariable String phoneNumber,
            @RequestBody CustomerManagementModel updatedFields) {

        CustomerManagementModel result = service.updateCustomerInfo(
                phoneNumber,
                updatedFields.getName(),
                updatedFields.getEmail(),
                updatedFields.getGender(),
                updatedFields.getBirthday(),
                updatedFields.isActive()
        );

        if (result == null) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(result);
    }

    @DeleteMapping("/{phoneNumber}")
    public ResponseEntity<Void> deleteCustomer(@PathVariable String phoneNumber) {
        service.deleteCustomerByPhone(phoneNumber);
        return ResponseEntity.ok().build();
    }
}
