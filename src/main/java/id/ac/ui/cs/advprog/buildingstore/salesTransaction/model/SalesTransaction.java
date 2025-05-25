package id.ac.ui.cs.advprog.buildingstore.salesTransaction.model;

import id.ac.ui.cs.advprog.buildingstore.CustomerManagement.model.CustomerManagementModel;
import id.ac.ui.cs.advprog.buildingstore.auth.model.User;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@NamedEntityGraph(
        name = "SalesTransaction.withCashierCustomerItemsProduct",
        attributeNodes = {
                @NamedAttributeNode("cashier"),
                @NamedAttributeNode("customer"),
                @NamedAttributeNode(value = "items", subgraph = "itemsWithProduct")
        },
        subgraphs = {
                @NamedSubgraph(
                        name = "itemsWithProduct",
                        attributeNodes = {
                                @NamedAttributeNode("product")
                        }
                )
        }
)
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

    //private int customerPhone;
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "customer_id", referencedColumnName = "id", nullable = false)
    private CustomerManagementModel customer;

    @Enumerated(EnumType.ORDINAL)
    private TransactionStatus status;

    @OneToMany(mappedBy = "transaction", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SalesItem> items = new ArrayList<>();
}
