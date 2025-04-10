package id.ac.ui.cs.advprog.buildingstore.ProductManagement.controller;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.service.ProductManagementService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")
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
    public ResponseEntity<ProductManagementModel> updateProduct(@PathVariable Integer id,
                                                                @RequestBody ProductManagementModel updatedProduct) {
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
}
