package hellojpa.Domain.ManyToManyExample;

import hellojpa.Domain.Locker;
import hellojpa.Domain.Team;
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
@AllArgsConstructor
@NoArgsConstructor
public class MemberManyToMany {
    @Id
    @GeneratedValue
    private Long id;
    @Column(name = "USERNAME")
    private String name;

    @ManyToOne(targetEntity = Team.class) //다대일
    @JoinColumn(name = "TEAM_ID")
    private Team team; //멤버 입장에서는 Many니까.
    //이 레퍼런스와 팀의 아이디랑 조인을 해야하니까. @JoinColumn 사용

    @OneToOne //ManyToOne과 동일하다.
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

//    @ManyToMany
//    @JoinTable(name = "MEMBER_PRODUCT") //이렇게 하면 연결 테이블이 PK가 FK가 되는 구조로 풀어낸다.
//    private List<Product> products = new ArrayList<>();

    @OneToMany(mappedBy = "member")
    private List<MemberProduct> memberProducts = new ArrayList<>();
}
