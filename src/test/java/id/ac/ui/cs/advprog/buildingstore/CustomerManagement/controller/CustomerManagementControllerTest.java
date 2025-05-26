package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service.CustomerManagementService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

public class CustomerManagementControllerTest {

    private MockMvc mockMvc;
    private CustomerManagementService service;
    private ObjectMapper objectMapper;
    private CustomerManagementModel customer;
    private CustomerManagementController controller;

    @BeforeEach
    void setUp() {
        service = Mockito.mock(CustomerManagementService.class);
        controller = new CustomerManagementController(service);
        mockMvc = MockMvcBuilders.standaloneSetup(controller).build();
        objectMapper = new ObjectMapper();
        
        customer = CustomerManagementModel.builder()
                .name("Alice")
                .phoneNumber("08123456789")
                .email("alice@example.com")
                .gender("Female")
                .birthday(new Date())
                .isActive(true)
                .build();
    }

    @Test
    void testAddCustomerSuccess() throws Exception {
        when(service.addCustomer(any())).thenReturn(customer);

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"))
                .andExpect(jsonPath("$.name").value("Alice"));
    }

    @Test
    void testGetCustomerByPhone() throws Exception {
        when(service.getCustomerByPhone("08123456789"))
                .thenReturn(Optional.of(customer));

        mockMvc.perform(get("/customers/08123456789"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("Alice"))
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"));
    }

    @Test
    void testUpdateCustomer() throws Exception {
        when(service.updateCustomerInfo(any(), any(), any(), any(), any(), any()))
                .thenReturn(customer);

        mockMvc.perform(put("/customers/08123456789")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.email").value("alice@example.com"))
                .andExpect(jsonPath("$.gender").value("Female"));
    }

    @Test
    void testDeleteCustomerByPhone() throws Exception {
        mockMvc.perform(delete("/customers/08123456789"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).deleteCustomerByPhone("08123456789");
    }

    @Test
    void testGetAllCustomers() throws Exception {
        // Create test data
        CustomerManagementModel customer1 = CustomerManagementModel.builder()
                .id(1)
                .name("Alice")
                .phoneNumber("08123456789")
                .email("alice@example.com")
                .gender("Female")
                .isActive(true)
                .build();
        
        CustomerManagementModel customer2 = CustomerManagementModel.builder()
                .id(2)
                .name("Bob")
                .phoneNumber("08123456788")
                .email("bob@example.com")
                .gender("Male")
                .isActive(true)
                .build();

        List<CustomerManagementModel> customers = Arrays.asList(customer1, customer2);
        
        // Set up mock service behavior
        when(service.getAllCustomers()).thenReturn(customers);

        // Perform GET request and verify response
        mockMvc.perform(get("/customers/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(2))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("Alice"))
                .andExpect(jsonPath("$[0].phoneNumber").value("08123456789"))
                .andExpect(jsonPath("$[1].id").value(2))
                .andExpect(jsonPath("$[1].name").value("Bob"))
                .andExpect(jsonPath("$[1].phoneNumber").value("08123456788"));
    }

    @Test
    void testGetAllCustomersEmptyList() throws Exception {
        // Set up mock service to return empty list
        when(service.getAllCustomers()).thenReturn(List.of());

        // Perform GET request and verify response
        mockMvc.perform(get("/customers/all"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(0))
                .andExpect(content().json("[]"));
    }

    @Test
    void testAddCustomerWithInvalidDataReturnsBadRequest() throws Exception {
        when(service.addCustomer(any())).thenReturn(null); // Service returns null for invalid data

        CustomerManagementModel invalidCustomer = CustomerManagementModel.builder()
                .phoneNumber("")
                .name("Invalid Customer")
                .build();

        mockMvc.perform(post("/customers")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(invalidCustomer)))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    void testGetCustomerByPhoneWhenNotFound() throws Exception {
        when(service.getCustomerByPhone("99999999")).thenReturn(Optional.empty());

        mockMvc.perform(get("/customers/99999999"))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testUpdateCustomerWhenNotFound() throws Exception {
        when(service.updateCustomerInfo(any(), any(), any(), any(), any(), any()))
                .thenReturn(null); // Customer not found

        mockMvc.perform(put("/customers/99999999")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(customer)))
                .andDo(print())
                .andExpect(status().isNotFound());
    }

    @Test
    void testDeleteNonExistentCustomerStillReturnsOk() throws Exception {
        // Even if customer doesn't exist, the delete operation should not fail
        doNothing().when(service).deleteCustomerByPhone("99999999");

        mockMvc.perform(delete("/customers/99999999"))
                .andDo(print())
                .andExpect(status().isOk());

        verify(service).deleteCustomerByPhone("99999999");
    }
}
