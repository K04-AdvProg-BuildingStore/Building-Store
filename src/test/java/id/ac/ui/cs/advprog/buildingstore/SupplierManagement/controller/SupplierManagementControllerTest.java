package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.repository.SupplierManagementRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase.Replace;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@SpringBootTest
@AutoConfigureMockMvc
@AutoConfigureTestDatabase(replace = Replace.ANY)  // uses H2
class SupplierManagementControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private SupplierManagementRepository supplierRepository;

    @Autowired
    private ObjectMapper objectMapper;

    private SupplierManagementModel supplier;

    @BeforeEach
    void setUp() {
        // clean slate
        supplierRepository.deleteAll();

        supplier = new SupplierManagementModel();
        supplier.setName("PT. Sumber Rejeki");
        supplier.setPhoneNumber("08123456789");
        supplier.setAddress("Jakarta");
        supplier.setActive(true);

        // preload one supplier for GET/PUT/DELETE tests
        supplier = supplierRepository.save(supplier);
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testAddSupplierSuccess() throws Exception {
        // new supplier payload
        SupplierManagementModel newSup = new SupplierManagementModel();
        newSup.setName("PT. Baru");
        newSup.setPhoneNumber("08120000000");
        newSup.setAddress("Bandung");
        newSup.setActive(false);

        mockMvc.perform(post("/suppliers")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(newSup)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.phoneNumber").value("08120000000"))
                .andExpect(jsonPath("$.name").value("PT. Baru"));

        // verify it landed in the DB
        Optional<SupplierManagementModel> saved =
                supplierRepository.findByPhoneNumber("08120000000");
        assertThat(saved).isPresent();
        assertThat(saved.get().getAddress()).isEqualTo("Bandung");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testGetSupplierByPhone() throws Exception {
        mockMvc.perform(get("/suppliers/{phone}", supplier.getPhoneNumber()))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.name").value("PT. Sumber Rejeki"))
                .andExpect(jsonPath("$.phoneNumber").value("08123456789"));
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testUpdateSupplier() throws Exception {
        // change only address and active flag
        supplier.setAddress("Surabaya");
        supplier.setActive(false);

        mockMvc.perform(put("/suppliers/{phone}", supplier.getPhoneNumber())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(supplier)))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.address").value("Surabaya"))
                .andExpect(jsonPath("$.active").value(false));

        // DB reflect
        SupplierManagementModel updated =
                supplierRepository.findByPhoneNumber(supplier.getPhoneNumber()).get();
        assertThat(updated.isActive()).isFalse();
        assertThat(updated.getAddress()).isEqualTo("Surabaya");
    }

    @WithMockUser(username = "admin", roles = {"ADMIN"})
    @Test
    void testDeleteSupplierByPhone() throws Exception {
        mockMvc.perform(delete("/suppliers/{phone}", supplier.getPhoneNumber()))
                .andExpect(status().isOk());

        assertThat(supplierRepository.findByPhoneNumber(supplier.getPhoneNumber()))
                .isEmpty();
    }
}