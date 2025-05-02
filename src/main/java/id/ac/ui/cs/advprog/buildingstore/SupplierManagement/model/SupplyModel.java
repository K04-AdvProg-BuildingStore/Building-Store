package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model;
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

    @Column(name = "supplied_product", nullable = false)
    private String suppliedProduct;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "supplyStock", nullable = false)
    private Integer supplyStock;

    @Column(name = "delivery_address", nullable = false)
    private String address;

}