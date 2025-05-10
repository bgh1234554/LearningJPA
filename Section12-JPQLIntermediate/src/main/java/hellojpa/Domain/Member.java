package hellojpa.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Entity
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
//extends BaseEntity - MappedSuperClass 사용법
@NamedQuery(name = "Member.findByName",
        query = "select m from Member m where m.name = :username")
public class Member extends BaseEntity {
    @Id
    @GeneratedValue
    @Column(name = "MEMBER_ID")
    private Long id;
    @Column(name = "USERNAME")
    private String name;
    private int age;

    @ManyToOne(targetEntity = Team.class, fetch = FetchType.LAZY) //다대일
    //즉시 로딩 - fetch - FetchType.EAGER
    @JoinColumn(name = "TEAM_ID")
    private Team team; //멤버 입장에서는 Many니까.
    //이 레퍼런스와 팀의 아이디랑 조인을 해야하니까. @JoinColumn 사용

    @OneToOne //ManyToOne과 동일하다.
    @JoinColumn(name = "LOCKER_ID")
    private Locker locker;

    //Period
    @Embedded
    private Period workPeriod;
//    private LocalDateTime startDate;
//    private LocalDateTime endDate;
//
    @Embedded
    private Address homeAddress;
     //Address 로 공통으로 쓰고 싶다 -> 임베디드 타입
//    private String city;
//    private String street;
//    private String zipcode;

    //잘 사용하진 않지만, 한 엔티티에서 같은 값 타입을 사용하면?
    @Embedded
    @AttributeOverrides({
            @AttributeOverride(name = "city", column = @Column(name = "WORK_CITY")),
            @AttributeOverride(name = "street", column = @Column(name = "WORK_STREET")),
            @AttributeOverride(name = "zipcode", column = @Column(name = "WORK_ZIPCODE"))
    })
    private Address workAddress;

    @Enumerated(EnumType.STRING)
    private MemberType memberType;

    @ElementCollection
    @CollectionTable(name = "FAVORITE_FOOD", joinColumns = @JoinColumn(name = "MEMBER_ID"))
    @Column(name = "FOOD_NAME") //저쪽 테이블 만들 때 FOOD_NAME으로 만들어줄 수 있도록.
    private Set<String> favoriteFoods = new HashSet<>();

//    @ElementCollection
//    @CollectionTable(name = "ADDRESS", joinColumns = @JoinColumn(name = "MEMBER_ID"))
//    private List<Address> addressHistory = new ArrayList<>();

    //값 타입 컬렉션보다 훨씬 유리하다
    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "MEMBER_ID")
    private List<AddressEntity> addressHistory = new ArrayList<>();

    //연관관계 편의 메서드 -> set 이름 쓰는거보단 특수한 이름 써서 구분되게 짓는 것이 좋다.
    public void changeTeam(Team team){
        this.team = team;
        team.getMembers().add(this);
    }
}
