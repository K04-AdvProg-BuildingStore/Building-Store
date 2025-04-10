package id.ac.ui.cs.advprog.buildingstore.ProductManagement.service;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;

import java.util.List;
import java.util.Optional;

public interface ProductManagementService {
    ProductManagementModel addProduct(ProductManagementModel product);

    Optional<ProductManagementModel> getProductById(Integer id);

    void deleteProductById(Integer id);

    ProductManagementModel updateProductInfo(Integer id, String name, Integer price,
                                             Integer quantity, String status, String information);

    List<ProductManagementModel> getAllProducts();
}
