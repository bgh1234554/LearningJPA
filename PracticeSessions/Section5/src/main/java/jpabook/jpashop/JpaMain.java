package jpabook.jpashop;

import jakarta.persistence.*;
import jpabook.jpashop.domain.Member;
import jpabook.jpashop.domain.Order;

public class JpaMain {

    public static void main(String[] args) {
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();
        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            //1주차 코드의 문제점
            Order order = em.find(Order.class, 1L);
            Long membrerId = order.getMemberId();

            Member member = em.find(Member.class, membrerId);
//            -> 뭔가 객체지향스럽지 않다...
//            order 테이블 안에서 Member 객체를 통으로 찾을 수 있으면 좋을텐데...
//            order.getMember();와 같이...
//            참조로 쭉쭉 찾아가는게 좋은데, DB처럼 식별자가 따로 있으면 끊겨버린다.

            /*
            1주차에 한 설계
            관계형 DB에 맞춰서 객체를 그대로 설계하였다.
            참조값을 찾아와서 객체를 통으로 뽑아내면 좋은데, 테이블의 외래키를 객체에 그대로 가져온다.
            참조가 다 끊기기 때문에 잘못된 매핑
            => 해결 방법 - 연관관계 매핑
             */
            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close();
        }
        emf.close();
    }
}
