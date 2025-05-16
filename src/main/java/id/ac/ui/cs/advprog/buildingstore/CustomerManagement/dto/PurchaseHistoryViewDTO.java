package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.dto;

import lombok.*;
import jakarta.persistence.SqlResultSetMapping;
import jakarta.persistence.ConstructorResult;
import jakarta.persistence.ColumnResult;

@SqlResultSetMapping(
    name = "PurchaseHistoryViewDTOMapping",
    classes = @ConstructorResult(
        targetClass = PurchaseHistoryViewDTO.class,
        columns = {
            @ColumnResult(name = "transactionId", type = Integer.class),
            @ColumnResult(name = "productName", type = String.class),
            @ColumnResult(name = "quantity", type = Integer.class)
        }
    )
)
@Data
@AllArgsConstructor
@NoArgsConstructor
public class PurchaseHistoryViewDTO {
    private Integer transactionId;
    private String productName;
    private Integer quantity;
}