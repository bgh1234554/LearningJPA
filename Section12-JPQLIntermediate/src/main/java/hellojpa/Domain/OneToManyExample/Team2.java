package hellojpa.Domain.OneToManyExample;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team2 {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany
    @JoinColumn(name = "TEAM_ID") //이렇게 하면 1대다에서 1이 주인이다.
    private List<Member2> members = new ArrayList<>();
}
