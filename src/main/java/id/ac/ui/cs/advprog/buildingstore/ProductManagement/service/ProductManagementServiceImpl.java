package id.ac.ui.cs.advprog.buildingstore.ProductManagement.service;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProductManagementServiceImpl implements ProductManagementService {

    private final ProductManagementRepository repository;

    public ProductManagementServiceImpl(ProductManagementRepository repository) {
        this.repository = repository;
    }

    @Override
    public ProductManagementModel addProduct(ProductManagementModel product) {
        if (product.getName() == null) {
            return null;
        }
        return repository.save(product);
    }

    @Override
    public Optional<ProductManagementModel> getProductById(Integer id) {
        return repository.findById(id);
    }

    @Override
    public void deleteProductById(Integer id) {
        if (!repository.existsById(id)) {
            throw new IllegalArgumentException("Product not found");
        }
        repository.deleteById(id);
    }


    @Override
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

    @Override
    public List<ProductManagementModel> getAllProducts() {
        return repository.findAll();
    }
    @Override
    public List<ProductManagementModel> searchProductsByName(String name) {
        return repository.findByNameContainingIgnoreCase(name);
    }
    @Override
    public void setProductStock(Integer productId, Integer newQuantity) {
        ProductManagementModel product = repository.findById(productId)
                .orElseThrow(() -> new IllegalArgumentException("Product not found"));

        product.setQuantity(newQuantity);
        product.setStatus(newQuantity > 0 ? "Available" : "Out of Stock");
        repository.save(product);
    }


}