package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter //세터는 굳이 다 안만들어도 된다. 언제든지 값 변경 가능하면 코드 추적하기 힘드니까.
@Entity
public class Member extends BaseEntity{

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "MEMBER_ID") //회사마다 컬럼을 대문자로 쓰는지 대문자로 쓰는지 다르다.
    private Long id;
    private String name;
    private String city;
    private String street;
    private String zipcode;

    @OneToMany(mappedBy = "member")
    private List<Order> orders = new ArrayList<>(); //관례상으로 new ArrayList<>(); NullPointerException 방지
}
