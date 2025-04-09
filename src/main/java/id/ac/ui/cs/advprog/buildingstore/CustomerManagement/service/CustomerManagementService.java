package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.service;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.repository.CustomerManagementRepository;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Date;


public class CustomerManagementService {

    private final CustomerManagementRepository repository;
    
    public CustomerManagementService(CustomerManagementRepository repository) {
        this.repository = repository;
    }

    public CustomerManagementModel addCustomer(CustomerManagementModel customer){
        if (customer.getPhoneNumber( ) == null || customer.getPhoneNumber().isBlank()){
            return null;
        }
        return repository.save(customer);
    }
    public Optional<CustomerManagementModel> getCustomerByPhone(String phoneNumber) {
        return repository.findByPhoneNumber(phoneNumber);
    }

    public void deleteCustomerByPhone(String phoneNumber) {
        repository.deleteByPhoneNumber(phoneNumber);
    }

    public CustomerManagementModel updateCustomerInfo(String phoneNumber, String name, String email,
                                                      String gender, Date birthday, Boolean isActive) {
        Optional<CustomerManagementModel> optionalCustomer = repository.findByPhoneNumber(phoneNumber);

        if (optionalCustomer.isPresent()) {
            CustomerManagementModel customer = optionalCustomer.get();

            if (name != null) customer.setName(name);
            if (email != null) customer.setEmail(email);
            if (gender != null) customer.setGender(gender);
            if (birthday != null) customer.setBirthday(birthday);
            if (isActive != null) customer.setActive(isActive);

            return repository.save(customer);
        }

        return null; // or throw a custom exception
    }

    
}
