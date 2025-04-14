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

        try{
            //1. 영속 관계 보여주기
//            Member member = new Member();
//            member.setId(100L);
//            member.setName("HelloJPA");
//            //여기까지는 JPA와 아무 상관없는 비영속 상태
//            //여기서 쿼리문을 날려도 안보인다.
//            System.out.println("BEFORE");
//            em.persist(member);
//            //em.detach(member); 영속성 컨텍스트에서 다시 지운다. 관계에서 지운다.
//            //em.remove(member); 실제 DB 삭제를 요청하는 상태.
//            System.out.println("AFTER");
//            //영속 상태가 되었다. 이제 영속 context라는 곳에서 멤버가 관리가 된다는 뜻이다.
//            //이때 DB에 저장이 안된다. - 출력해보면 BEFORE와 AFTER 다음에 INSERT 쿼리문이 날라간 것을 확인할 수 있다.

            //2. 1차 캐시 예제
//            Member member = new Member();
//            member.setId(101L);
//            member.setName("HelloJPA2");
//            System.out.println("BEFORE");
//            em.persist(member);
//            System.out.println("AFTER");
//
//            Member findMember = em.find(Member.class, 101L);
//            //SELECT 쿼리가 날라가지 않고 em의 1차 캐시에서 조회한다.
//            System.out.println("findMember.id = "+findMember.getId());
//            System.out.println("findMember.name = "+findMember.getName());
//
//            //쿼리문이 한번만 나간다. 한번 조회후 영속성 컨텍스트의 1차 캐시에 올리니까.
//            Member findMember1 = em.find(Member.class, 1L);
//            Member findMember2 = em.find(Member.class, 1L);
//
//            //영속 엔티티의 동일성 보장
//            System.out.println(findMember1==findMember2); //true 리턴! 영속 엔티티의 동일성 보장

            //3. 쓰기 지연 예제 (일단 Member.java의 주석 내용부터 보기)
//            Member member1 = new Member(150L, "A");
//            Member member2 = new Member(160L, "B");
//
//            //여기서는 영속성 컨텍스트에 계속 쌓이게 된다. 이후 커밋 시에 DB에 쿼리가 날라간다.
//            em.persist(member1);
//            em.persist(member2);
//            System.out.println("=========="); //쿼리 언제 나가는지 확인용
//            /*
//            Q - 왜 그렇게 하는거지?
//            A - 버퍼링이란 기능 사용 가능.
//            모았다가 DB에 쌓여있는 여러 데이터를 한번에 보낼 수 있다.
//             */
//            //변경 감지
//            Member findMember = em.find(Member.class, 150L);
//            findMember.setName("ZZZ"); //값만 바꾸면 UPDATE 쿼리문이 날라간다.
//            //여기서 persist 호출을 해야하나? -> 그럴 필요 없다. JPA는 데이터를 값을 자바 컬렉션처럼 관리해주기 때문에.

            //4. 플러시 예제
//            Member member = new Member(200L,"member200");
//            em.persist(member);
//
//            em.flush(); //커밋 이전에 강제로 DB에 보내는 것.
//            System.out.println("==========");

            //5. 준영속 상태 예제
            //가져오면 영속성 컨텍스트에 이 멤버를 저장한다.
            Member findMember = em.find(Member.class, 150L);
            findMember.setName("AAAA"); //이후 변경 감지를 통해 UPDATE 쿼리를 날려준다.
            //더 이상 영속성 컨텍스트에서 관리하고 싶지 않으면...
            //em.detach(findMember); //이후에 커밋해도 이름이 AAAA로 안바뀐다.
            //직접 쓸일은 별로 없다...
            em.clear(); //영속성 컨텍스트를 통으로 초기화 - 테스트 케이스를 작성할 때 많이 쓰게 된다.
            Member findMember2 = em.find(Member.class, 150L);
            //이렇게 되면 SELECT 쿼리문이 2번 나가게 된다.
            System.out.println("==========");
            tx.commit(); //바로 이때 쿼리문이 날라가게 된다. (1번 관련 설명)
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close(); //사용 다 하면 꼭 닫기
        }
        //실제로는 스프링이 알아서 다 해주기 때문에 em.persist만 하면 된다.
        emf.close(); //WAS가 내려갈때 emf를 닫아줘야 한다.
    }
}
