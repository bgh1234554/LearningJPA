package hellojpa.Domain.ManyToManyExample;

import jakarta.persistence.*;

import java.time.LocalDateTime;

@Entity
public class MemberProduct {

    @Id @GeneratedValue
    private Long id;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private MemberManyToMany member;

    @ManyToOne
    @JoinColumn(name = "PRODUCT_ID")
    private ProductManyToMany product;

    private int amount;
    private int price;

    private LocalDateTime orderDateTime;
}
