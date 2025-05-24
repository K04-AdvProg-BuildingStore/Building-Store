package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model;
import lombok.*;
import jakarta.persistence.*;
import java.util.Date;

@Data
@Entity
@Builder
@AllArgsConstructor
@NoArgsConstructor
@Table(name = "purchase_history")

public class PurchaseHistoryModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;


    // Foreign-like key from SalesTransaction
    @Column(nullable = false)
    private String phoneNumber;

    private String itemName;

    private int quantity;

    private double totalAmount;

    @Temporal(TemporalType.TIMESTAMP)
    private Date purchaseDate;

    
}
