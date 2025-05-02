package id.ac.ui.cs.advprog.buildingstore.salesTransaction.controller;

import id.ac.ui.cs.advprog.buildingstore.auth.model.Role;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import id.ac.ui.cs.advprog.buildingstore.auth.repository.UserRepository;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto.SalesTransactionCreateRequest;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import id.ac.ui.cs.advprog.buildingstore.salesTransaction.repository.SalesTransactionRepository;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.security.test.context.support.WithMockUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class SalesTransactionControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SalesTransactionRepository transactionRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private User savedCashier;

    @BeforeEach
    void setUp() {
        transactionRepository.deleteAll();
        userRepository.deleteAll();

        savedCashier = User.builder()
                .username("cashier1")
                .password("password")
                .role(Role.CASHIER)
                .build();

        savedCashier = userRepository.save(savedCashier);
    }

    @WithMockUser(username = "testuser", roles = {"CASHIER"})
    @Test
    void testCreateTransaction() throws Exception {
        SalesTransactionCreateRequest request = SalesTransactionCreateRequest.builder()
                .cashierId(savedCashier.getId())
                .customerPhone(812345678)
                .status("PENDING")
                .build();

        mockMvc.perform(post("/api/sales-transactions")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(request)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.customerPhone").value(812345678))
                .andExpect(jsonPath("$.status").value("PENDING"))
                .andExpect(jsonPath("$.cashierUsername").value("cashier1"));
    }

    @WithMockUser(username = "testuser", roles = {"CASHIER"})
    @Test
    void testGetAllTransactions() throws Exception {
        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(savedCashier)
                .customerPhone(800111223)
                .status("COMPLETE")
                .build();
        transactionRepository.save(transaction);

        mockMvc.perform(get("/api/sales-transactions"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$", hasSize(1)))
                .andExpect(jsonPath("$[0].status").value("COMPLETE"));
    }

    @WithMockUser(username = "testuser", roles = {"CASHIER"})
    @Test
    void testDeleteTransaction() throws Exception {
        SalesTransaction transaction = SalesTransaction.builder()
                .cashier(savedCashier)
                .customerPhone(811000111)
                .status("PENDING")
                .build();
        transaction = transactionRepository.save(transaction);

        mockMvc.perform(delete("/api/sales-transactions/" + transaction.getId()))
                .andExpect(status().isNoContent());
    }
}
