package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.SalesTransaction;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class SalesItemRequest {
    private SalesTransaction transaction;
    private Integer productId;
    private int quantity;
    private int price;
}
