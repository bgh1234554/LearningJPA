package hellojpa.Domain.Cascade;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Child {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "USERNAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "PARENT_ID")
    private Parent parent;
}
