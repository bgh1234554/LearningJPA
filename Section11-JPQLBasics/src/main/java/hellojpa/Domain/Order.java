package hellojpa.Domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name = "ORDERS")
public class Order {
    @Id @GeneratedValue
    private Long id;
    private int orderAmount;
    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private Product product;
    @Embedded
    private Address address;
}
