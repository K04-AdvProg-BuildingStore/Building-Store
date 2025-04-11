package id.ac.ui.cs.advprog.buildingstore.payment.model;

import java.util.UUID;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;


@Entity
@Getter @Setter
@Builder @NoArgsConstructor @AllArgsConstructor
public class Payment {
    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    private int amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String method;

    private String salesTransactionId;
}
