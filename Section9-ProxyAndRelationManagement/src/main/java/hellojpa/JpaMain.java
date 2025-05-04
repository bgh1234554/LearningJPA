package hellojpa;

import hellojpa.Domain.Cascade.Child;
import hellojpa.Domain.Cascade.Parent;
import hellojpa.Domain.Item;
import hellojpa.Domain.Member;
import hellojpa.Domain.Movie;
import hellojpa.Domain.OneToManyExample.Member2;
import hellojpa.Domain.OneToManyExample.Team2;
import hellojpa.Domain.Team;
import jakarta.persistence.*;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
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
            //0. 프록시가 필요한 이유
//            Member member = new Member();
//            member.setName("aa");
//            member.setTeam(new Team());
//            em.persist(member);
//            printMember(member);
//            //여기선 굳이 team 정보까지 가져오면 손해니까...
//            //-> 이 문제 해결 위한 방법이 프록시와 지연 로딩
//            Member member2 = em.find(Member.class, member.getId());
//            printMemberAndTeam(member2);

            //1. .getReference() 예제
//            Member member = new Member();
//            member.setName("aa");
//            em.persist(member);
//
//            em.flush(); em.clear();
//
// //            Member findMember = em.find(Member.class, member.getId());
//            Member findMember = em.getReference(Member.class, member.getId());
//            System.out.println("findMember = " + findMember.getClass());
//            //Member$HibernateProxy$.... -> 가짜 클래스
//            System.out.println("findMember.Id = " + findMember.getId());
//            System.out.println("findMember.name = " + findMember.getName());
//            //println이 주석처리가 안되어있을 때만, SELECT 쿼리가 안나간다.
//            //실제로 사용될 때만 쿼리를 날린다. Id는 직접 파라미터로 넣은거니까 쿼리를 날릴 필요가 없는데...

            //2. 프록시 특징 - instanceof 사용
//            Member member1 = new Member();
//            member1.setName("aa");
//            em.persist(member1);
//
//            Member member2 = new Member();
//            member2.setName("bb");
//            em.persist(member2);
//
//            em.flush(); em.clear();
//
//            Member m1 = em.find(Member.class, member1.getId());
//            Member m2 = em.getReference(Member.class, member2.getId());
//
//            //find == find - true, find == getReference - false
//            System.out.println("m1 == m2:" + (m1.getClass() == m2.getClass()));
//            //실제 비즈니스로직에서는 동등한지 비교할 때 실제 엔티티가 넘어올 지,
//            //프록시가 넘어올지 알 수 없다.
//            //그래서 타입 비교할때는 == 비교가 아닌 instanceof로 한다.
//            System.out.println("m1 instanceof Member:" + (m1 instanceof Member));
//            System.out.println("m1 instanceof Member:" + (m2 instanceof Member));

            //3. 준영속 상태 시 문제
//            Member member1 = new Member();
//            member1.setName("aa");
//            em.persist(member1);
//            em.flush(); em.clear();
//            Member refMember = em.getReference(Member.class, member1.getId()); //proxy
//            System.out.println("refMember = " + refMember.getClass());
// //            String name = refMember.getName();
//            //다른 메서드로 초기화하는 것이 아닌, 초기화용 메서드
//            Hibernate.initialize(refMember); //강제 초기화 - Hibernate에서 제공, JPA는 제공하지 않음.
//            System.out.println("isLoaded = "+ emf.getPersistenceUnitUtil().isLoaded(refMember));
//            //em.detach(refMember); //중간에 영속성 컨텍스트에서 끄집어내면??
//            //em.close() //영속성 컨텍스트를 닫아버린다면??
//            //em.clear() //끈건 아니지만 클리어 해버리면 도움을 받을 수 없으니까...
//
//            //System.out.println("refMember = " + refMember.getName());
//            //영속성 컨텍스트를 통해서 초기화하는데, 더 이상 영속성 컨텍스트가 관리를 안하니까...
            //4. 지연로딩 vs 즉시로딩
//            Team team = new Team();
//            team.setName("team1");
//            em.persist(team);
//
//            Member member1 = new Member();
//            member1.setName("aa");
//            member1.setTeam(team);
//            em.persist(member1);
//
//            em.flush(); em.clear();
//
//            Member m = em.find(Member.class, member1.getId());
//            System.out.println("m ="+m.getTeam().getClass()); //proxy
//            System.out.println("=====");
//            m.getTeam().getName(); //여기서 쿼리가 나간다.
//            //지연로딩 - 연관된 것은 프록시로
//            System.out.println("=====");
            //5. Cascade 예제
            //이렇게 하면 Parent 중심으로 하고 싶은데...
            //parent를 persist하면 child가 자연스럽게 persist되면 좋겠다 -> CASCADE
            Child child1 = new Child();
            Child child2 = new Child();
            Parent parent = new Parent();
            parent.addChild(child1);
            parent.addChild(child2);

            em.persist(parent);
//            em.persist(child1);
//            em.persist(child2);

            em.flush(); em.clear();

            Parent findParent = em.find(Parent.class, parent.getId());
            findParent.getChildList().remove(0);
            //child가 하나 지워진다.

            //em.remove(findParent); //이러면 연관된 다른 자식들까지 다 지워진다.
            //CascadeType.ALL + orphanRemoval=true
            //Parent를 persist하고 remove하면서 생명주기를 관리하면, 딸려있는 child도 알아서 관리된다.
            tx.commit();
        } catch (Exception e){
            tx.rollback();
            e.printStackTrace();
        } finally{
            em.close(); //사용 다 하면 꼭 닫기
        }
        //실제로는 스프링이 알아서 다 해주기 때문에 em.persist만 하면 된다.
        emf.close(); //WAS가 내려갈때 emf를 닫아줘야 한다.
    }

    private static void printMember(Member member) {
        System.out.println("member ="+member.getName());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getName();
        Team team = member.getTeam();
        System.out.println("username = " + username);
        System.out.println("team = " + team);
    }
}
