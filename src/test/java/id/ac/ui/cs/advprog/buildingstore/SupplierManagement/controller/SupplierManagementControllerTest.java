package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.service.SupplierManagementService;
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

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SupplierManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private SupplierManagementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplierManagementModel supplier;

    @BeforeEach
    void setUp() {
        supplier = new SupplierManagementModel();
        supplier.setId(1);
        supplier.setName("PT. Sumber Rejeki");
        supplier.setPhoneNumber("08123456789");
        supplier.setAddress("Jakarta");
        supplier.setActive(true);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddSupplierSuccess() throws Exception {
        Mockito.when(service.addSupplier(any())).thenReturn(supplier);

        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"))
                .andExpect(jsonPath("$.name").value("PT. Sumber Rejeki"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetSupplierByPhone() throws Exception {
        Mockito.when(service.getSupplierByPhone("08123456789"))
                .thenReturn(Optional.of(supplier));

        mockMvc.perform(get("/suppliers/08123456789"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PT. Sumber Rejeki"))
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateSupplier() throws Exception {
        Mockito.when(service.updateSupplierInfo(any(), any(), any(), any()))
                .thenReturn(supplier);

        mockMvc.perform(put("/suppliers/08123456789")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Jakarta"))
                .andExpect(jsonPath("$.active").value(true));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteSupplierByPhone() throws Exception {
        mockMvc.perform(delete("/suppliers/08123456789"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).deleteSupplierByPhone("08123456789");
    }
}