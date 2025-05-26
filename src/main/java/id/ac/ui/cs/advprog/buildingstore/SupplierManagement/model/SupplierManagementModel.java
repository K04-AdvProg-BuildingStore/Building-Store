package id.ac.ui.cs.advprog.buildingstore.SupplierManagement.model;
import jakarta.persistence.*;
import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "supplier")

public class SupplierManagementModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    @Column(name = "name", nullable = false)
    private String name;

    @Column(name = "phone_number", unique = true, nullable = false)
    private String phoneNumber;

    @Column(name = "address", nullable = false)
    private String address;

    @Column(name = "active", nullable = false)
    private boolean active;

    public void activateSupplier() {
        this.setActive(true);
    }

    public void deactivateSupplier() {
        this.setActive(false);
    }
}