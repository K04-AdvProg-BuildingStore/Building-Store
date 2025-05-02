package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class SalesItemResponse {
    private int id;
    private int productId;
    private int transactionId;
    private int quantity;
    private int price;
}
