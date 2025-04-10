package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sales_item")
public class SalesItem {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private String id;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "product_id", referencedColumnName = "id")
    private ProductManagementModel product;

    private int quantity;

    private int price;

    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "transaction_id", referencedColumnName = "id")
    private SalesTransaction transaction;
}
