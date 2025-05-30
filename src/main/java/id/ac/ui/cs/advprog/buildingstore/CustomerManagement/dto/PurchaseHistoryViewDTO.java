package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto;

public interface PurchaseHistoryViewDTO {
    Integer getCustomerId();
    String getCustomerName();
    String getPhoneNumber();
    Integer getTransactionId();
    Integer getProductId();
    Integer getQuantity();
    Double getPrice();
}