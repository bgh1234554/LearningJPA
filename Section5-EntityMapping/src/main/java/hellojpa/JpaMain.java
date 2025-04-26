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
            //1. 필드와 컬럼 매핑 부분
//            Member member = new Member();
//            member.setId(1L);
//            member.setUsername("A");
//            member.setRoleType(RoleType.USER);
//            Member member = new Member();
//            member.setId(2L);
//            member.setUsername("B");
//            member.setRoleType(RoleType.ADMIN);
            //EnumType.Ordinal이면 USER면 0, ADMIN이면 1이 저장된다..
            //쓰면 안되는 이유 - 만약에 RoleType의 0번째로 Guest가 추가된다고 해보면??
//            Member member = new Member();
//            member.setId(3L);
//            member.setUsername("C");
//            member.setRoleType(RoleType.GUEST);
            //A도 0이고 C도 0이다!! -> 옛날 데이터가 변경되지 않으니까.
            //EnumType을 다시 String으로 바꾸면 hibernate.hbm2ddl.auto을 다시 create로 바꿔줘야 한다.

            //2. 기본 키 매핑
//            Member member = new Member();
//            member.setUsername("D"); //ID = 1
//            member.setUsername("E"); //ID = 2
            //SEQUENCE 전략 매핑
//            Member member = new Member();
//            member.setUsername("F");
//            System.out.println("====");
//            em.persist(member);
//            System.out.println("member.id = " + member.getId());
//            System.out.println("====");

            //Sequence 매핑의 Allocation size 관련 예제

            //이게 예전에는 Member.java의 allocationSize와 DB의 incremented by가 달라도 됐는데,
            //Hibernate가 6버전으로 업데이트되면서 안되는 것 같다.
            //Copilot - "allocationSize와 데이터베이스 시퀀스의 increment by 값이 반드시 일치해야 합니다."

            Member member1 = new Member();
            Member member2 = new Member();
            Member member3 = new Member();

            member1.setUsername("A");
            member2.setUsername("B");
            member3.setUsername("C");

            System.out.println("====");

            em.persist(member1); //1, 51 (처음은 더미로 호출)
            em.persist(member2); //MEM에서 호출
            em.persist(member3); //MEM
            /*
            Allocation Size : 한번 호출할때 DB에 먼저 일정 사이즈 만큼 할당받아 올려놓고 그 개수만큼 쓰는 방식

            allocationSize = 50이면 Hibernate가 시퀀스를 50개씩 미리 땡겨와서 메모리에 저장해 놓고,
            그 50개를 메모리에서 빠르게 꺼내서 ID를 부여하는 것. DB 시퀀스를 매번 부르지 않고, 한 번에 50개를 확보.

            강사님 - 근데 그렇게 하면 웹서버를 내리는 시점에 그게 날라가는 거죠. 그 사이에 구멍이 생기는 거다.
            해석
            - allocationSize 최적화를 쓸 때 웹서버(=애플리케이션)가 꺼질 경우,
            미리 땡겨온 ID(시퀀스 번호) 중에 아직 안 쓴 것들이 버려진다.
            예시
            예를 들어 1~50번까지 미리 확보 -> 5번 쓰고 나서 갑자기 서버를 내렸다
            -> 6~50번까지 ID는 아직 안 쓴 상태였는데, 이걸 다시 쓸 수가 없다.
            -> 51번부터 땡겨와 구멍이 생긴다고 표현한 것.
             */
            System.out.println("member1.id = " + member1.getId());
            System.out.println("member2.id = " + member2.getId());
            System.out.println("member3.id = " + member3.getId());

            System.out.println("====");
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
