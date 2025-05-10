package hellojpa.Domain.ManyToManyExample;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Table(name = "PRODUCT_MANYTOMANY")
public class ProductManyToMany {
    @Id @GeneratedValue
    private Long id;
    private String name;

//    @ManyToMany(mappedBy = "products")
//    private List<MemberManyToMany> members = new ArrayList<>();

    @OneToMany(mappedBy = "product")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
