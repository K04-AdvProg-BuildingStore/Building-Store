package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.PurchaseHistoryModel;
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

import java.util.Date;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc
public class PurchaseHistoryControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private PurchaseHistoryService service;

    @Autowired
    private ObjectMapper objectMapper;

    private PurchaseHistoryModel samplePurchase;

    @BeforeEach
    void setUp() {
        samplePurchase = PurchaseHistoryModel.builder()
                .phoneNumber("08123456789")
                .itemName("Cement")
                .quantity(5)
                .totalAmount(500000.0)
                .purchaseDate(new Date())
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddPurchaseSuccess() throws Exception {
        Mockito.when(service.addPurchase(any())).thenReturn(samplePurchase);

        mockMvc.perform(post("/purchase-history")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(samplePurchase)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.itemName").value("Cement"))
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetPurchaseHistoryByPhoneNumber() throws Exception {
        Mockito.when(service.getPurchaseHistory("08123456789"))
                .thenReturn(List.of(samplePurchase));

        mockMvc.perform(get("/purchase-history/08123456789"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].itemName").value("Cement"))
                .andExpect(jsonPath("$[0].phoneNumber").value("08123456789"));
    }
}
