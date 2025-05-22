package id.ac.ui.cs.advprog.buildingstore.salesTransaction.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionCreateRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.service.SalesTransactionService;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.CustomerManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

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
    @Mock
    private CustomerManagementRepository customerRepository;
    @InjectMocks
    private SalesTransactionController controller;
    private SalesTransactionCreateRequest request;
    private User mockCashier;
    private CustomerManagementModel mockCustomer;
    private SalesTransaction mockTransaction;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        mockCashier = new User();
        mockCashier.setId(9952);
        mockCustomer = CustomerManagementModel.builder().id(816998556).build();
        request = SalesTransactionCreateRequest.builder()
                .cashierId(9952)
                .customerId(816998556)
                .status(TransactionStatus.PENDING)
                .items(null)
                .build();
        mockTransaction = SalesTransaction.builder()
                .id(1)
                .cashier(mockCashier)
                .customer(mockCustomer)
                .status(TransactionStatus.PENDING)
                .items(null)
                .build();
    }

    @Test
    void testCreateRequest() throws Exception {
        when(userRepository.findById(9952)).thenReturn(Optional.of(mockCashier));
        when(customerRepository.findById(816998556)).thenReturn(Optional.of(mockCustomer));
        when(transactionService.createTransaction(eq(mockCashier), eq(mockCustomer), eq(TransactionStatus.PENDING), any())).thenReturn(mockTransaction);
        mockMvc.perform(post("/api/v2/sales-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerId").value(816998556));
    }

    @Test
    void testUpdateRequest() throws Exception {
        when(userRepository.findById(9952)).thenReturn(Optional.of(mockCashier));
        when(customerRepository.findById(816998556)).thenReturn(Optional.of(mockCustomer));
        when(transactionService.updateTransaction(eq(1), eq(mockCashier), eq(mockCustomer), eq(TransactionStatus.PENDING), any())).thenReturn(mockTransaction);
        mockMvc.perform(put("/api/v2/sales-transactions/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.customerId").value(816998556));
    }

    @Test
    void testDeleteRequest() throws Exception {
        doNothing().when(transactionService).deleteTransaction(1);
        mockMvc.perform(delete("/api/v2/sales-transactions/1"))
                .andExpect(status().isNoContent());
    }
}

