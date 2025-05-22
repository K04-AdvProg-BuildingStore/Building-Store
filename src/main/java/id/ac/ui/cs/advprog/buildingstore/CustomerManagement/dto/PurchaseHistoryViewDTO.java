package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto;

public interface PurchaseHistoryViewDTO {
    String getCustomerName();
    String getPhoneNumber();
    Integer getTransactionId();
    Integer getStatus(); // changed from String to Integer
    Integer getProductId(); // changed from String to Integer
    Integer getQuantity();
    Double getPrice();
}