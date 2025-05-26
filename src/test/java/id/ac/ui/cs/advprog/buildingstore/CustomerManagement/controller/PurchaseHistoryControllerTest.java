package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTO;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto.PurchaseHistoryViewDTOImpl;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.PurchaseHistoryService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;

import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class PurchaseHistoryControllerTest {

    private MockMvc mockMvc;
    private PurchaseHistoryService service;
    private PurchaseHistoryController controller;
    private PurchaseHistoryViewDTO dto1, dto2;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(PurchaseHistoryService.class);
        controller = new PurchaseHistoryController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        
        // Initialize DTOs 
        dto1 = new PurchaseHistoryViewDTOImpl(1, "Alice", "0811111111", 101, 201, 2, 10000.0);
        dto2 = new PurchaseHistoryViewDTOImpl(1, "Alice", "0811111111", 102, 202, 1, 5000.0);
    }

    @Test
    void testGetPurchaseHistoryByIdReturnsDTOs() throws Exception {
        when(service.getCustomerPurchaseHistoryById(1))
                .thenReturn(List.of(dto1, dto2));

        mockMvc.perform(get("/api/purchase-history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Alice"))
                .andExpect(jsonPath("$[0].transactionId").value(101))
                .andExpect(jsonPath("$[0].productId").value(201))
                .andExpect(jsonPath("$[0].quantity").value(2))
                .andExpect(jsonPath("$[0].price").value(10000.0))
                .andExpect(jsonPath("$[1].transactionId").value(102))
                .andExpect(jsonPath("$[1].productId").value(202));
    }

    @Test
    void testGetPurchaseHistoryByIdReturnsEmptyList() throws Exception {
        when(service.getCustomerPurchaseHistoryById(anyInt()))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/purchase-history/999"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }
    
    @Test
    void testGetPurchaseHistoryByIdWithUserRole() throws Exception {
        when(service.getCustomerPurchaseHistoryById(1))
                .thenReturn(List.of(dto1));

        mockMvc.perform(get("/api/purchase-history/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].customerId").value(1))
                .andExpect(jsonPath("$[0].customerName").value("Alice"));
    }

    @Test
    void testGetPurchaseHistoryWithNegativeIdReturnsEmptyList() throws Exception {
        when(service.getCustomerPurchaseHistoryById(-1))
                .thenReturn(List.of());

        mockMvc.perform(get("/api/purchase-history/-1"))
                .andExpect(status().isOk())
                .andExpect(content().json("[]"));
    }

    @Test
    void testGetPurchaseHistoryWithServiceException() throws Exception {
        when(service.getCustomerPurchaseHistoryById(anyInt()))
                .thenThrow(new RuntimeException("Database error"));

        mockMvc.perform(get("/api/purchase-history/1"))
                .andExpect(status().isInternalServerError());
    }
}