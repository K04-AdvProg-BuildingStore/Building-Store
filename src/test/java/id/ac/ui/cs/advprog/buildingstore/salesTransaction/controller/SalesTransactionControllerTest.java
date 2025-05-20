package id.ac.ui.cs.advprog.buildingstore.salesTransaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesItemRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionCreateRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.service.SalesTransactionService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@ExtendWith(MockitoExtension.class)
class SalesTransactionControllerTest {

    private MockMvc mockMvc;

    private ObjectMapper objectMapper = new ObjectMapper();

    @Mock
    private SalesTransactionService transactionService;

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private SalesTransactionController salesTransactionController;

    private SalesTransactionCreateRequest request;
    private User mockCashier;
    private SalesTransaction mockTransaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(salesTransactionController).build();

        SalesItemRequest item1 = new SalesItemRequest(null, 2, 100000);
        SalesItemRequest item2 = new SalesItemRequest(null, 1, 200000);
        request = new SalesTransactionCreateRequest(9952, 816998556, "CANCELLED", List.of(item1, item2));

        mockCashier = new User();
        mockTransaction = SalesTransaction.builder()
                .id(1)
                .cashier(mockCashier)
                .customerPhone(816998556)
                .status("CANCELLED")
                .items(List.of())
                .build();
    }

    @Test
    void testCreateTransaction() throws Exception {
        when(userRepository.findById(9952)).thenReturn(Optional.of(mockCashier));
        when(transactionService.createTransaction(mockCashier, 816998556, "CANCELLED", request.getItems()))
                .thenReturn(mockTransaction);

        mockMvc.perform(post("/api/sales-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.customerPhone").value(816998556));
    }

    @Test
    void testUpdateTransaction() throws Exception {
        when(userRepository.findById(9952)).thenReturn(Optional.of(mockCashier));
        when(transactionService.updateTransaction(eq(1), eq(mockCashier), eq(816998556), eq("CANCELLED"), anyList()))
                .thenReturn(mockTransaction);

        mockMvc.perform(put("/api/sales-transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("CANCELLED"))
                .andExpect(jsonPath("$.customerPhone").value(816998556));
    }

    @Test
    void testDeleteTransaction() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1);

        mockMvc.perform(delete("/api/sales-transactions/1"))
                .andExpect(status().isNoContent());
    }
}
