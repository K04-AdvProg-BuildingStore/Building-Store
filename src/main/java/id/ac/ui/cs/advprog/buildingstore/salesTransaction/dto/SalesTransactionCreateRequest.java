package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import lombok.Builder;
import lombok.Data;


@Data
@Builder
public class SalesTransactionCreateRequest {
    private Integer cashierId;
    private int customerPhone;
    private String status;
}
