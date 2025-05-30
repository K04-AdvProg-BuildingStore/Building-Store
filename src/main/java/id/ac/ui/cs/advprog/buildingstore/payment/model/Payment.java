package id.ac.ui.cs.advprog.buildingstore.payment.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

import id.ac.ui.cs.advprog.buildingstore.payment.enums.PaymentStatus;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.ForeignKey;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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

    private BigDecimal amount;

    @Enumerated(EnumType.STRING)
    private PaymentStatus status;

    private String method;

    @Column(name = "sales_transaction_id")
    @JoinColumn(name = "sales_transaction_id", referencedColumnName = "id",
            foreignKey = @ForeignKey(name = "fk_payment_sales_transaction"))
    private Integer salesTransactionId;

    @Column(name = "created_at")
    private LocalDateTime createdAt = LocalDateTime.now();
}
