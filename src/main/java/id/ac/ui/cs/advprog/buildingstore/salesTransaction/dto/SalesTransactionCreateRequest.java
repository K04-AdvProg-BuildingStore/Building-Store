package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SalesTransactionCreateRequest {
    private Integer cashierId;
    private int customerPhone;
    private String status;
    private List<SalesItemRequest> items;

    public SalesTransactionCreateRequest(Integer cashierId, int customerPhone, String status, List<SalesItemRequest> items) {
        this.cashierId = cashierId;
        this.customerPhone = customerPhone;
        this.status = status;
        this.items = items;
    }
}
