package id.ac.ui.cs.advprog.buildingstore.salesTransaction.dto;

import id.ac.ui.cs.advprog.buildingstore.salesTransaction.model.TransactionStatus;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@Builder
public class SalesTransactionCreateRequest {
    private Integer cashierId;
    private Integer customerId;
    private TransactionStatus status;
    private List<SalesItemRequest> items;
}
