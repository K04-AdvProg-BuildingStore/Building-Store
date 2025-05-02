package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SalesTransactionResponse {
    private Integer id;
    private Integer customerPhone;
    private String status;
    private String cashierUsername;
}
