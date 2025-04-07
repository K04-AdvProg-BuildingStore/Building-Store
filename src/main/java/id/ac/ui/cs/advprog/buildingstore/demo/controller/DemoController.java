package id.ac.ui.cs.advprog.buildingstore.demo.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping( "api/v1/demo-controller")
public class DemoController {

    @GetMapping
    public ResponseEntity<String> sayHello(){
        return ResponseEntity.ok("Hello World!");
    }

    @GetMapping("/cashier")
    @PreAuthorize("hasRole('CASHIER')")
    public ResponseEntity<String> cashierOnly() {
        return ResponseEntity.ok("Cashier WORKS LESGOOO!!!!");
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<String> adminOnly() {
        return ResponseEntity.ok("Admin WORKS LEGOOOO!!!!");
    }

    @GetMapping("/shared")
    @PreAuthorize("hasAnyRole('CASHIER', 'ADMIN')")
    public ResponseEntity<String> sharedAccess() {
        return ResponseEntity.ok("BOTH ADMIN AND CASHIER WORKS FOR THIS ONE FUNCTION, YAYAYAYAYAYAY");
    }
}
