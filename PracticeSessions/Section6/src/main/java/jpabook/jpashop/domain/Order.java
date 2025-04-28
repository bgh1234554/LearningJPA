package jpabook.jpashop.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@Table(name = "ORDERS") //DB마다 ORDER를 테이블 이름으로 설정할 수 없는 곳이 있어서
public class Order {

    @Id @GeneratedValue
    @Column(name = "ORDER_ID")
    private Long id;

//    @Column(name = "MEMBER_ID")
//    private Long memberId;

    @ManyToOne
    @JoinColumn(name = "MEMBER_ID")
    private Member member;

    @OneToMany(mappedBy = "order") //orderItem에 있는 order가 주인이니까.
    private List<OrderItem> orderItems = new ArrayList<>();

    private LocalDateTime orderDate;
    //SpringBoot에서는 DB에 컬럼을 생성할 때 어떤 관례 (ORDER_DATE, order_date)를 따를지 직접 설정할 수 있다.
    //하이버네이트는 순수하게 사용하면 필드 이름 그대로 DB에 저장하지만, 스프링부트는 알아서 order_date로 바꿔준다.

    @Enumerated(EnumType.STRING)
    private OrderStatus status;

    //연관관계 편의 메서드 만들기
    public void addOrderItem(OrderItem orderItem) {
        orderItem.setOrder(this);
        orderItems.add(orderItem);
    }
}
