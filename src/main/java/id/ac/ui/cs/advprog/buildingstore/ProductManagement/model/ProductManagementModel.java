package id.ac.ui.cs.advprog.buildingstore.ProductManagement.model;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "products")
public class ProductManagementModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(nullable = false)
    private String name;

    private Integer quantity;

    private Integer price;

    private String status; // e.g., Available, Out of Stock

    @Column(length = 1000)
    private String information;

    @ManyToOne
    @JoinColumn(name = "administrator_id")
    private User administrator;
    // Optional logic methods
    public boolean isAvailable() {
        return "Available".equalsIgnoreCase(this.status) && (this.quantity != null && this.quantity > 0);
    }


    public void markOutOfStock() {
        this.quantity=0;
        this.status = "Out of Stock";
    }

    public void restock(int additionalQuantity) {
        if (this.quantity == null) this.quantity = 0;
        this.quantity += additionalQuantity;
        this.status = "Available";
    }
}
