package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.repository.ProductManagementRepository;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplyRepository;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplierManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)
public class SupplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierManagementRepository supplierRepository;

    @Autowired
    private ProductManagementRepository productRepository;

    @Autowired
    private SupplyRepository supplyRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplierManagementModel supplier;
    private ProductManagementModel product;
    private SupplyModel supply;

    @BeforeEach
    void setUp() {
        // Clean DB
        supplyRepository.deleteAll();
        productRepository.deleteAll();
        supplierRepository.deleteAll();

        // Create supplier
        supplier = new SupplierManagementModel();
        supplier.setName("Test Supplier");
        supplier.setPhoneNumber("08123450001");
        supplier.setAddress("Addr");
        supplier.setActive(true);
        supplier = supplierRepository.save(supplier);

        // Create product
        product = ProductManagementModel.builder()
                .name("Test Product")
                .quantity(100)
                .price(1000)
                .status("Available")
                .information("Info")
                .administrator(null)
                .build();
        product = productRepository.save(product);

        // Create supply
        supply = new SupplyModel();
        supply.setSupplier(supplier);
        supply.setProduct(product);
        supply.setSupplyStock(50);
        supply.setDeliveryAddress("Warehouse A");
        supply = supplyRepository.save(supply);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void listAllSupplies_shouldReturnSavedSupply() throws Exception {
        mockMvc.perform(get("/api/supplies"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(supply.getId()))
                .andExpect(jsonPath("$[0].supplier.phoneNumber").value(supplier.getPhoneNumber()))
                .andExpect(jsonPath("$[0].product.id").value(product.getId()));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getSupplyById_shouldReturnSupply() throws Exception {
        mockMvc.perform(get("/api/supplies/{id}", supply.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(supply.getId()))
                .andExpect(jsonPath("$.deliveryAddress").value("Warehouse A"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getBySupplierPhone_shouldReturnSupplyList() throws Exception {
        mockMvc.perform(get("/api/supplies/by-supplier-phone/{phone}", supplier.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplier.id").value(supplier.getId()));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void getByProduct_shouldReturnSupplyList() throws Exception {
        mockMvc.perform(get("/api/supplies/by-product/{productId}", product.getId()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].product.id").value(product.getId()));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void createSupply_shouldPersistAndReturn() throws Exception {
        // Clear existing
        supplyRepository.deleteAll();

        SupplyModel toCreate = new SupplyModel();
        SupplierManagementModel supRef = new SupplierManagementModel();
        supRef.setPhoneNumber(supplier.getPhoneNumber());
        toCreate.setSupplier(supRef);
        ProductManagementModel prodRef = new ProductManagementModel();
        prodRef.setId(product.getId());
        toCreate.setProduct(prodRef);
        toCreate.setSupplyStock(30);
        toCreate.setDeliveryAddress("Warehouse B");

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplier.phoneNumber").value(supplier.getPhoneNumber()))
                .andExpect(jsonPath("$.product.id").value(product.getId()))
                .andExpect(jsonPath("$.supplyStock").value(30));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void updateSupply_shouldChangeFields() throws Exception {
        SupplyModel updates = new SupplyModel();
        updates.setSupplyStock(100);

        mockMvc.perform(put("/api/supplies/{id}", supply.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.supplyStock").value(100));

        SupplyModel updated = supplyRepository.findById(supply.getId()).orElseThrow();
        assertThat(updated.getSupplyStock()).isEqualTo(100);
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void deleteSupply_shouldRemoveRecord() throws Exception {
        mockMvc.perform(delete("/api/supplies/{id}", supply.getId()))
                .andExpect(status().isNoContent());

        assertThat(supplyRepository.findById(supply.getId())).isEmpty();
    }
}