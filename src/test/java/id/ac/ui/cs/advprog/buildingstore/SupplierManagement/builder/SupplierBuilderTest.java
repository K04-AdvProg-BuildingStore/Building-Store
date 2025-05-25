package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.builder;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;
import org.junit.jupiter.api.Test;

import java.util.function.Supplier;

import static org.junit.jupiter.api.Assertions.*;


class SupplierBuilderTest {

    @Test
    void testSetters() {
        SupplierBuilder builder = new SupplierBuilder();
        builder.setName("John Doe")
                .setPhoneNumber("081234567890")
                .setAddress("123 Main St")
                .setActive(true);

        assertEquals("John Doe", builder.build().getName());
        assertEquals("081234567890", builder.build().getPhoneNumber());
        assertEquals("123 Main St", builder.build().getAddress());
        assertEquals(true, builder.build().isActive());
    }

    @Test
    void testBuildCreatesSupplier() {
        SupplierBuilder builder = new SupplierBuilder();
        SupplierManagementModel SupplierManagementModel= builder.setName("John Doe")
                .setPhoneNumber("081234567890")
                .setAddress("123 Main St")
                .setActive(true)
                .build();

        assertNotNull(SupplierManagementModel.class);
        assertEquals("John Doe", SupplierManagementModel.getName());
        assertEquals("081234567890", SupplierManagementModel.getPhoneNumber());
        assertEquals("123 Main St", SupplierManagementModel.getAddress());
        assertEquals(true, SupplierManagementModel.isActive());
    }

    @Test
    void testBuildThrowsExceptionWhenRequiredFieldsAreMissing() {
        SupplierBuilder builder = new SupplierBuilder();
        builder.setName("John Doe")
                .setAddress("123 Main St");

        Exception exception = assertThrows(IllegalStateException.class, builder::build);
        assertEquals("Required fields are missing", exception.getMessage());
    }
}