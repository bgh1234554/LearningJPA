package hellojpa.Domain;

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
//extends BaseEntity - MappedSuperClass 사용법
public class Member extends BaseEntity {
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

    //연관관계 편의 메서드 -> set 이름 쓰는거보단 특수한 이름 써서 구분되게 짓는 것이 좋다.
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
