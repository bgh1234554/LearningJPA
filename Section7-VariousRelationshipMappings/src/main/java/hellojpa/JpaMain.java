package hellojpa;

import hellojpa.Domain.Member;
import hellojpa.Domain.OneToManyExample.Member2;
import hellojpa.Domain.OneToManyExample.Team2;
import hellojpa.Domain.Team;
import jakarta.persistence.*;

import java.util.List;

public class JpaMain {

    public static void main(String[] args) {
        //Persistence 클래스가 EntityManagerFactory를 생성한다.
        //앱 로딩 시점에 하나만 만들어야 한다.
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        //EntityManagerFactory가 필요할 때마다 EntityManager을 찍어낸다.
        //트랜잭션 단위마다 em을 만들어줘야 한다. em - 자바 커넥션 같은 존재 - 내 객체를 대신 저장해준다.
        EntityManager em = emf.createEntityManager();
        //이 안에서 실제 동작하는 코드를 작성하게 된다.
        //근데 뭐라도 만들어야 DB에 넣고 빼니까 테이블을 만들어보자 -> Member.java

        EntityTransaction tx = em.getTransaction();
        tx.begin();

        try{
            //1. 일대다 매핑
            Member2 member = new Member2();
            //실무에서는 생성자나 빌더 패턴으로 만든다.
            member.setName("member1");
            em.persist(member);

            Team2 team = new Team2();
            team.setName("team1");
            //여기서 어떻게...?
            team.getMembers().add(member);
            em.persist(team);
            //DB에 값은 정상적인데 쿼리가 여러개 나간다.
            //팀 엔티티를 저장하는데, 옆 테이블을 업데이트 하기 때문에 멤버에 대한 업데이트 쿼리가 하나 더 나간다.
            //진짜 안좋은 점 - DB 구조를 깊이있게 모르면 팀 테이블을 업데이트했는데 왜 멤버 테이블이 업데이트가 되는거지?
            //같은 일이 발생하기 때문에 운영이 힘들어진다.
            //이럴거면 다대일에서 양방향 관계로 만드는게 낫다.
            tx.commit(); //바로 이때 쿼리문이 날라가게 된다.
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close(); //사용 다 하면 꼭 닫기
        }
        //실제로는 스프링이 알아서 다 해주기 때문에 em.persist만 하면 된다.
        emf.close(); //WAS가 내려갈때 emf를 닫아줘야 한다.
    }
}
