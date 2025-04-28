package hellojpa;

import hellojpa.Domain.Member;
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
            //1. 객체 키를 테이블에 맞춰 모델링할 경우의 문제
//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeamId(team.getId());
//            em.persist(member);
//            //뭔가 객체지향스럽지 않지만 돌아가기는 한다.
//            //외래키 식별자를 직접 다뤄서 조회하는 것이 자연스럽지 않다.
//
//            //조회할때도 문제 발생
//            Member findMember = em.find(Member.class, member.getId());
//            Long findTeamId = findMember.getTeamId();
//            Team findTeam = em.find(Team.class, findTeamId);
//            //연관관계가 없으면 이렇게 무한으로 JPA한테 물어봐야하고, DB에 접속해야 한다.
//            //JAVA에서는 자연스럽지 않은 방법이다.

            //2. @ManyToOne 사용 후에는?
//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeam(team); //자연스럽다!
//            em.persist(member);
//
// //            em.flush();
// //            em.clear();
// //            강제로 DB에서 가져오게 쿼리를 보고 싶다면 삽입.
//
//            Member findMember = em.find(Member.class, member.getId());
//            Team findTeam = findMember.getTeam(); //다시 물어볼 필요 없이 바로
//            System.out.println(findTeam.getName());
//            //정상적으로 출력이 됨을 확인할 수 있다.
//            //다만 영속성 컨텍스트에서 처럼 1차 쿼리에서 조회하는 것이기 때문에,
//            //실행 결과에는 SELECT 쿼리 같은 것이 보이지 않는다.

            //3. 양방향 연관관계 기본 예제
//            Member member = new Member();
//            member.setName("member1");
//            member.setTeam(team); //자연스럽다!
//            em.persist(member);
//            Member member2 = new Member();
//            member2.setName("member2"); member2.setTeam(team);
//            em.persist(member2);
//
//            em.flush(); em.clear();
//
//            Member findMember = em.find(Member.class, member.getId());
//            //여기서 양방향 매핑이 나온다. (물론 객체는 단방향이 편하지만)
//            List<Member> members = findMember.getTeam().getMembers();
//
//            System.out.println("====");
//            for(Member m : members){
//                System.out.println("m = "+m.getName());
//            }
//            System.out.println("====");

            //4. 양방향 매핑 시 가장 많이 하는 실수
            //4-1. 잘못된 주인에 입력하기
            //저장
//            Member member = new Member();
//            member.setName("member1");
//            em.persist(member);
//
//            Team team = new Team();
//            team.setName("teamA");
// //            team.getMembers().add(member);
// //            //가짜... INSERT 쿼리문을 넣어도 멤버가 주인이기 때문에 업데이트가 되지 않는다!!
//            em.persist(team);
//
//            member.setTeam(team);
//            em.flush(); em.clear();
            //4-2. 양쪽에 넣어주는 것이 더 좋은 이유
            Team team = new Team();
            team.setName("TeamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);
            em.persist(member);

            team.getMembers().add(member); //객체지향적 입장에서 양쪽에 넣기.

//            em.flush(); em.clear();

            //위 둘중 하나만 주석인 경우, 둘 다 주석인 경우를 모두 고려해 돌려보자.

            //member.changeTeam(team); 연관관계 편의 메서드
            //물론 team에다가 addMember와 같은 메서드를 정할 수도 있다.

            Team findTeam = em.find(Team.class, team.getId()); //이미 1차 캐시에 있는 것 로딩
            //flush가 없다면 팀이 아까 저장한 팀이 그대로 영속성 컨텍스트에 들어가 있기 때문에,
            //아무것도 뜨지 않는다.
            List<Member> members = findTeam.getMembers();

            System.out.println("=======");
            for(Member m : members){
                System.out.println("m = "+m.getName());
            }
            System.out.println("=======");

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
