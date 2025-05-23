package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model;

import id.ac.ui.cs.advprog.buildingstore.ProductManagement.model.ProductManagementModel;
import jakarta.persistence.*;
import lombok.*;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supply")
public class SupplyModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    // link back to the supplier
    @ManyToOne(optional = false)
    @JoinColumn(name = "supplier_id", nullable = false)
    private SupplierManagementModel supplier;

    // link to the product being supplied
    @ManyToOne(optional = false)
    @JoinColumn(name = "product_id", nullable = true)
    private ProductManagementModel product;

    @Column(name = "supply_stock", nullable = false)
    private Integer supplyStock;

    @Column(name = "delivery_address", nullable = false)
    private String deliveryAddress;
}
