package id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Entity
@Table(name = "customers")
public class CustomerManagementModel {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Integer id;

    private String name;

    @Column(unique = true, nullable = false)
    private String phoneNumber;  // Used as primary identifier

    private String email;

    private String gender;

    @Temporal(TemporalType.DATE)
    private Date birthday;

    private boolean isActive;

    // Optional logic methods for testing or service layer
    public void activateCustomer() {
        this.setActive(true);
    }

    public void deactivateCustomer() {
        this.setActive(false);
    }
}
