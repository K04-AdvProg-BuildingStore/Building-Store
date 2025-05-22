package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplyModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service.SupplyService;
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

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplyService service;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplierManagementModel supplier;
    private SupplyModel supply;

    @BeforeEach
    void setUp() {
        supplier = new SupplierManagementModel();
        supplier.setId(1);
        supplier.setName("Test Supplier");
        supplier.setPhoneNumber("555-0001");
        supplier.setAddress("Addr");
        supplier.setActive(true);

        supply = new SupplyModel();
        supply.setId(10);
        supply.setSupplier(supplier);
        supply.setSupplyStock(50);
        supply.setDeliveryAddress("Warehouse A");
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testListAllSupplies() throws Exception {
        Mockito.when(service.getAllSupplies()).thenReturn(List.of(supply));

        mockMvc.perform(get("/api/supplies"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].supplier.phoneNumber").value("555-0001"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testGetSupplyById() throws Exception {
        Mockito.when(service.getSupplyById(10)).thenReturn(supply);

        mockMvc.perform(get("/api/supplies/10"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10))
                .andExpect(jsonPath("$.deliveryAddress").value("Warehouse A"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testGetBySupplierPhone() throws Exception {
        Mockito.when(service.getSuppliesBySupplierPhone("555-0001"))
                .thenReturn(List.of(supply));

        mockMvc.perform(get("/api/supplies/by-supplier-phone/555-0001"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].supplier.id").value(1));
    }

    // Uncomment when by-product endpoint is enabled
    // @WithMockUser(username = "user", roles = {"USER"})
    // @Test
    // void testGetByProduct() throws Exception {
    //     Mockito.when(service.getSuppliesByProduct(5))
    //         .thenReturn(List.of(supply));
    //
    //     mockMvc.perform(get("/api/supplies/by-product/5"))
    //         .andDo(print())
    //         .andExpect(status().isOk())
    //         .andExpect(jsonPath("$[0].id").value(10));
    // }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testCreateSupply() throws Exception {
        SupplyModel toCreate = new SupplyModel();
        toCreate.setSupplier(supplier);
        toCreate.setSupplyStock(50);
        toCreate.setDeliveryAddress("Warehouse A");

        Mockito.when(service.createSupply(any(SupplyModel.class))).thenReturn(supply);

        mockMvc.perform(post("/api/supplies")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(toCreate)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(10));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testUpdateSupply() throws Exception {
        SupplyModel updates = new SupplyModel();
        updates.setDeliveryAddress("New Addr");

        Mockito.when(service.updateSupply(eq(10), any(SupplyModel.class)))
                .thenReturn(supply);

        mockMvc.perform(put("/api/supplies/10")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(updates)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.deliveryAddress").value("Warehouse A"));
    }

    @WithMockUser(username = "user", roles = {"USER"})
    @Test
    void testDeleteSupply() throws Exception {
        mockMvc.perform(delete("/api/supplies/10"))
                .andDo(print())
                .andExpect(status().isNoContent());

        verify(service).deleteSupply(10);
    }
}
