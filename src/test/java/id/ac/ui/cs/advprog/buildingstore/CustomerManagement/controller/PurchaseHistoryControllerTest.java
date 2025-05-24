package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTOImpl;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.PurchaseHistoryService;
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

import static org.mockito.ArgumentMatchers.anyString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseHistoryService service;

    private PurchaseHistoryViewDTO dto1, dto2;

    @BeforeEach
    void setUp() {
        dto1 = new PurchaseHistoryViewDTOImpl("Alice", "0811111111", 1, 1, 2, 10000.0); // productId, quantity, price
        dto2 = new PurchaseHistoryViewDTOImpl("Alice", "0811111111", 2, 2, 1, 5000.0); // productId, quantity, price
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetPurchaseHistoryReturnsDTOs() throws Exception {
        Mockito.when(service.getCustomerPurchaseHistory("0811111111"))
                .thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/purchase-history/0811111111"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerName").value("Alice"))
                .andExpect(jsonPath("$[0].productId").value(1))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[1].productId").value(2))
                .andExpect(jsonPath("$[1].quantity").value(1));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetPurchaseHistoryReturnsEmptyList() throws Exception {
        Mockito.when(service.getCustomerPurchaseHistory(anyString()))
                .thenReturn(List.of());

        mockMvc.perform(get("/purchase-history/0000000000"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
}