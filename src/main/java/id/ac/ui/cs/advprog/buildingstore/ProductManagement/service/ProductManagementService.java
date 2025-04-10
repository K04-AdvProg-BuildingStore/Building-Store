package id.ac.ui.cs.advprog.buildingstore.ProductManagement.service;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductManagementService {

    private final ProductManagementRepository repository;

    public ProductManagementService(ProductManagementRepository repository) {
        this.repository = repository;
    }

    public ProductManagementModel addProduct(ProductManagementModel product) {
        if (product.getName() == null) {
            return null;
        }
        return repository.save(product);
    }

    public Optional<ProductManagementModel> getProductById(Integer id) {
        return repository.findById(id);
    }

    public void deleteProductById(Integer id) {
        repository.deleteById(id);
    }

    public ProductManagementModel updateProductInfo(
            Integer id,
            String name,
            Integer price,
            Integer quantity,
            String status,
            String information
    ) {
        Optional<ProductManagementModel> optionalProduct = repository.findById(id);
        if (optionalProduct.isEmpty()) {
            return null;
        }

        ProductManagementModel existingProduct = optionalProduct.get();
        existingProduct.setName(name);
        existingProduct.setPrice(price);
        existingProduct.setQuantity(quantity);
        existingProduct.setStatus(status);
        existingProduct.setInformation(information);

        return repository.save(existingProduct);
    }

    public List<ProductManagementModel> getAllProducts() {
        return repository.findAll();
    }
}
