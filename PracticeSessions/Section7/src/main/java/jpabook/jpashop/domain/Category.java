package jpabook.jpashop.domain;

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
public class Category {

    @Id
    @GeneratedValue
    private Long id;

    private String name;

    //셀프 매핑
    @ManyToOne //자식 입장에선 부모가 하나니까
    @JoinColumn(name = "PARENT_ID")
    private Category parent;

    @OneToMany(mappedBy = "parent")
    private List<Category> child = new ArrayList<>();

    @ManyToMany
    //중간 테이블 매핑 과정
    @JoinTable(name = "CATEGORY_ITEM",
            //내가 조인 하는 것
            joinColumns = @JoinColumn(name = "CATEGORY_ID"),
            //반대쪽이 조인 하는 것
            inverseJoinColumns = @JoinColumn(name = "ITEM_ID"))
    private List<Item> items = new ArrayList<>();
}
