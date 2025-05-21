package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class PurchaseHistoryViewDTOImpl implements PurchaseHistoryViewDTO {
    private String customerName;
    private String phoneNumber;
    private Integer transactionId;
    private Integer status; // changed from Enum to Integer
    private Integer productId;
    private Integer quantity;
    private Double price;
}
