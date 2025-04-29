package jpabook.jpashop;

import jakarta.persistence.*;
import jpabook.jpashop.domain.*;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            //2주차 코드 -> 연관관계 매핑을 씌워보기
            //예시 코드 1 - 연관관계 편의 메서드 사용해보기
//            Order order = new Order();
//            order.addOrderItem(new OrderItem());
            //예시 코드 2 - 굳이 연관관계 편의 메서드 사용 없이
            Order order = new Order();
            OrderItem orderItem = new OrderItem();
            orderItem.setOrder(order);
            em.persist(orderItem);
            //양방향이 아니어도 앱 만드는데 아무 문제가 없다!
            //-> 개발상의 편의를 위해서

            //핵심: 할 수 있으면 최대한 단방향으로
            //실무에서는 조회나 JPQL 작성을 더 편하게 하기 위해
            //양쪽 방향으로 조회해야할 일이 많이 생기게 된다.
            //실무에선 양방향 아니면 그냥 두번 조회하면 된다.

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close();
        }
        emf.close();
    }
}
