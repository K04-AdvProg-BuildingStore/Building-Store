package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.CustomerManagementService;
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
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
public class CustomerManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CustomerManagementService service;

    @Autowired
    private ObjectMapper objectMapper;

    private CustomerManagementModel customer;

    @BeforeEach
    void setUp() {
        customer = CustomerManagementModel.builder()
                .name("Alice")
                .phoneNumber("08123456789")
                .email("alice@example.com")
                .gender("Female")
                .birthday(new Date())
                .isActive(true)
                .build();
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddCustomerSuccess() throws Exception {
        Mockito.when(service.addCustomer(any())).thenReturn(customer);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetCustomerByPhone() throws Exception {
        Mockito.when(service.getCustomerByPhone("08123456789"))
                .thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/08123456789"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateCustomer() throws Exception {
        Mockito.when(service.updateCustomerInfo(any(), any(), any(), any(), any(), any()))
                .thenReturn(customer);

        mockMvc.perform(put("/customers/08123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.gender").value("Female"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteCustomerByPhone() throws Exception {
        mockMvc.perform(delete("/customers/08123456789"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).deleteCustomerByPhone("08123456789");
    }
}
