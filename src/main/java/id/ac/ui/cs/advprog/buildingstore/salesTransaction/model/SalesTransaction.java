package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Table(name = "sales_transaction")
public class SalesTransaction {

    @Id
    @GeneratedValue
    private Integer id;

    @ManyToOne
    @JoinColumn(name = "cashier_id")
    private User cashier;

    private int customerPhone;

    private String status;

}
