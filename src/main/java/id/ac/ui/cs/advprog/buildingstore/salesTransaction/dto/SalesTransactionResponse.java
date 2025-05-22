package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SalesTransactionResponse {
    private Integer id;
    private Integer customerId;
    private TransactionStatus status;
    private String cashierUsername;
    private List<SalesItemResponse> items;

}
