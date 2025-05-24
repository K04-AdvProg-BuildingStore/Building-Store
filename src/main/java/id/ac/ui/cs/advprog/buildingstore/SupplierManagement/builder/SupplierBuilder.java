package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.builder;

import id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model.SupplierManagementModel;

public class SupplierBuilder {
    private String name;
    private String phoneNumber;
    private String address;
    private boolean active;

    public SupplierBuilder setName(String name) {
        this.name = name;
        return this;
    }

    public SupplierBuilder setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
        return this;
    }

    public SupplierBuilder setAddress(String address) {
        this.address = address;
        return this;
    }

    public SupplierBuilder setActive(boolean active) {
        this.active = active;
        return this;
    }

    public SupplierManagementModel build() {
        if (name == null || phoneNumber == null || address == null) {
            throw new IllegalStateException("Required fields are missing");
        }
        return new SupplierManagementModel(null, name, phoneNumber, address, active);
    }

}