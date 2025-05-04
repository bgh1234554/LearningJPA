package hellojpa.Domain.OneToManyExample;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Member2 {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "USERNAME")
    private String name;

    @ManyToOne
    @JoinColumn(name = "TEAM_ID", insertable = false, updatable = false)
    //일대다 양방향을 편법으로 만드는 방법
    //뒤에 두 옵션을 쓰지 않으면 둘다 연관관계의 주인이 되어버리기 때문에 써야 한다.
    //읽기 전용으로 만들어버리는 것이다.
    private Team2 team;
}
