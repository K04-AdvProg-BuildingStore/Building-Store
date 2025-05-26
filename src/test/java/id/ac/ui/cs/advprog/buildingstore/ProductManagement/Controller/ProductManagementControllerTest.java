package id.ac.ui.cs.advprog.buildingstore.ProductManagement.Controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import id.ac.ui.cs.advprog.buildingstore.ProductManagement.service.ProductManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class ProductManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProductManagementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private ProductManagementModel product;

    @BeforeEach
    void setUp() {
        product = ProductManagementModel.builder()
                .id(1)
                .name("Concrete")
                .price(50000)
                .quantity(10)
                .status("Available")
                .information("Strong and reliable")
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddProductSuccess() throws Exception {
        Mockito.when(service.addProduct(any())).thenReturn(product);

        mockMvc.perform(post("/products")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Concrete"))
                .andExpect(jsonPath("$.price").value(50000));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetProductById() throws Exception {
        Mockito.when(service.getProductById(1)).thenReturn(Optional.of(product));

        mockMvc.perform(get("/products/1"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Concrete"))
                .andExpect(jsonPath("$.quantity").value(10));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateProductSuccess() throws Exception {
        Mockito.when(service.updateProductInfo(any(), any(), any(), any(), any(), any()))
                .thenReturn(product);

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(product)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("Available"))
                .andExpect(jsonPath("$.information").value("Strong and reliable"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteProductById() throws Exception {
        mockMvc.perform(delete("/products/1"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).deleteProductById(1);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetAllProducts() throws Exception {
        Mockito.when(service.getAllProducts()).thenReturn(List.of(product));

        mockMvc.perform(get("/products"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Concrete"));
    }
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetProductByIdNotFound() throws Exception {
        Mockito.when(service.getProductById(2)).thenReturn(Optional.empty());

        mockMvc.perform(get("/products/2"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteProductNotFound() throws Exception {
        Mockito.doThrow(new IllegalArgumentException("Product not found"))
                .when(service).deleteProductById(99);

        mockMvc.perform(delete("/products/99"))
                .andDo(print())
                .andExpect(status().isBadRequest())
                .andExpect(content().json("{\"error\":\"Product not found\"}"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateProductWithInvalidData() throws Exception {
        ProductManagementModel invalidProduct = ProductManagementModel.builder()
                .id(1)
                .name("") // Invalid name
                .price(-1) // Invalid price
                .quantity(-5)
                .status("")
                .information("")
                .build();

        mockMvc.perform(put("/products/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(invalidProduct)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

}
