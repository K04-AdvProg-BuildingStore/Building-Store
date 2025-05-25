package id.ac.ui.cs.advprog.buildingstore.ProductManagement.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.service.ProductManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.test.annotation.Rollback;

import java.util.List;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
@Rollback
public class ProductManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ProductManagementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductManagementModel product;

    @BeforeEach
    void setUp() {
        product = ProductManagementModel.builder()
                .name("Concrete")
                .price(50000)
                .quantity(10)
                .status("Available")
                .information("Strong and reliable")
                .build();
        product = service.addProduct(product);
    }


    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddProductSuccess() throws Exception {
        ProductManagementModel newProduct = ProductManagementModel.builder()
                .name("Bricks")
                .price(20000)
                .quantity(50)
                .status("Available")
                .information("Wall bricks")
                .build();

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newProduct)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Bricks"))
                .andExpect(jsonPath("$.price").value(20000));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetProductById() throws Exception {
        mockMvc.perform(get("/products/" + product.getId()))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Concrete"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateProductSuccess() throws Exception {
        product.setStatus("Out of Stock");
        product.setInformation("Updated info");

        mockMvc.perform(put("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Out of Stock"))
                .andExpect(jsonPath("$.information").value("Updated info"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteProductById() throws Exception {
        mockMvc.perform(delete("/products/" + product.getId()))
                .andDo(print())
                .andExpect(status().isOk());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetAllProducts() throws Exception {
        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Concrete"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetProductByIdNotFound() throws Exception {
        mockMvc.perform(get("/products/999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteProductNotFound() throws Exception {
        mockMvc.perform(delete("/products/999"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Product not found\"}"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateProductWithInvalidData() throws Exception {
        ProductManagementModel invalidProduct = ProductManagementModel.builder()
                .id(product.getId())
                .name("")
                .price(-100)
                .quantity(-5)
                .status("")
                .information("")
                .build();

        mockMvc.perform(put("/products/" + product.getId())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }
}
