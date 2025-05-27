package id.ac.ui.cs.advprog.buildingstore.ProductManagement.Controller;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.service.ProductManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/products")
@PreAuthorize("hasRole('ADMIN')")
@RequiredArgsConstructor
public class ProductManagementController {

    private final ProductManagementService service;

    @PostMapping
    public ResponseEntity<ProductManagementModel> addProduct(@RequestBody ProductManagementModel product) {
        ProductManagementModel created = service.addProduct(product);
        return ResponseEntity.ok(created);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ProductManagementModel> getProductById(@PathVariable Integer id) {
        return service.getProductById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity updateProduct(@PathVariable Integer id,
                                        @RequestBody ProductManagementModel updatedProduct) {

        if (updatedProduct.getName() == null || updatedProduct.getName().trim().isEmpty()) {
            throw new IllegalArgumentException("Product name cannot be empty");
        }
        if (updatedProduct.getPrice() < 0) {
            throw new IllegalArgumentException("Price cannot be negative");
        }
        if (updatedProduct.getQuantity() < 0) {
            throw new IllegalArgumentException("Quantity cannot be negative");
        }

        ProductManagementModel result = service.updateProductInfo(
                id,
                updatedProduct.getName(),
                updatedProduct.getPrice(),
                updatedProduct.getQuantity(),
                updatedProduct.getStatus(),
                updatedProduct.getInformation()
        );
        return ResponseEntity.ok(result);
    }


    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteProduct(@PathVariable Integer id) {
        service.deleteProductById(id);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<ProductManagementModel>> getAllProducts() {
        List<ProductManagementModel> products = service.getAllProducts();
        return ResponseEntity.ok(products);
    }
    @GetMapping("/search")
    public ResponseEntity<List<ProductManagementModel>> searchProductsByName(@RequestParam String name) {
        List<ProductManagementModel> results = service.searchProductsByName(name);
        return ResponseEntity.ok(results);
    }



    @ExceptionHandler(IllegalArgumentException.class)
    public ResponseEntity<?> handleIllegalArgument(IllegalArgumentException ex) {
        return ResponseEntity.badRequest().body(Map.of("error", ex.getMessage()));
    }
}