package hellojpa;

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

//        Member member = new Member();
//        member.setId(1L); member.setName("HelloA");
//        em.persist(member);
//        //이렇게만 하면 오류가 난다. Transaction이 있어야 테이블에 업데이트가 된다.
//
//        tx.commit();
//        em.close();
        //try-catch-finally를 쓰는 것이 훨씬 안전하다.
        try{
            //기본적인 CRUD with JPA
//            //1. 회원 등록
// //            Member member = new Member();
// //            member.setId(1L); member.setName("HelloA");
// //            em.persist(member);
//            //2. 회원 조회
//            Member findMember = em.find(Member.class,1L);
// //            System.out.println(findMember.getId());
// //            System.out.println(findMember.getName()); //SELECT 문이 잘 나간다.
//            //4. 회원 삭제
//            // //em.remove(findMember); //DELETE 문이 나간다.
//            //3. 회원 수정
//            findMember.setName("Jack");
//            //em.persist(findMember)가 필요 없다. 새롭게 저장이 필요없다. UPDATE 쿼리를 날리기 때문에.

            //JPQL - DB에 종속적이지 않게 객체를 대상으로 쿼리를 날릴 수 있게 서비스 제공.
            //Member 객체를 대상으로 쿼리를 저장한다. (특정 DB 언어에 종속되지 않게 짤 수 있다.)
            List<Member> result = em.createQuery("select m from Member as m", Member.class).getResultList();
//            List<Member> result2 = em.createQuery("select m from Member as m", Member.class)
//                    .setFirstResult(4).setMaxResults(8).getResultList(); //4번부터 8개 가져오기.
//            //이런식으로 짜면 각 DB에 맞춰서 알아서 번역해준다.
            for(Member m : result){
                System.out.println(m.getId() + " " + m.getName());
            };
            tx.commit(); //데이터 반영을 위해서
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close(); //사용 다 하면 꼭 닫기
        }
        //실제로는 스프링이 알아서 다 해주기 때문에 em.persist만 하면 된다.
        emf.close(); //WAS가 내려갈때 emf를 닫아줘야 한다.
    }
}
