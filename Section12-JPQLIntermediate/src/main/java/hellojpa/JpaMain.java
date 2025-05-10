package hellojpa;

import hellojpa.Domain.*;
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

        try {
            Team teamA = new Team();
            teamA.setName("TeamA");
            em.persist(teamA);

            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);

            Member member1 = new Member();
            member1.setName("Member1");
            member1.setTeam(teamA);
            em.persist(member1);

            Member member2 = new Member();
            member2.setName("Member2");
            member2.setTeam(teamA);
            em.persist(member2);

            Member member3 = new Member();
            member3.setName("Member3");
            member3.setTeam(teamB);
            em.persist(member3);

            em.flush(); em.clear();
            //1. 경로 표현식
//            //더 이상 갈 수 없는 필드 - 상태 필드, 경로 탐색의 끝
//            String query = "select m.name from Member m";
//            List<String> result = em.createQuery(query, String.class)
//                    .getResultList();
//            for(String s:result){
//                System.out.println(s);
//            }
//            //묵시적 내부 조인 -> 계속해서 탐색 가능 m.team.뭐뭐.
//            //쿼리를 보면 INNER JOIN이 발생한다. -> 조심해서 써야겠구나...
//            query = "select m.team from Member m";
//            List<Team> result2 = em.createQuery(query,Team.class).getResultList();
//            for(Team t:result2){
//                System.out.println("t = "+t);
//            }
//            //컬렉션 값 연관 경로 - 조인 발생하지만 계속 탐색 불가능
//            //members.하면 안나온다... 컬렉션 자체를 가리키니까
//            System.out.println("RESULT3");
//            query = "select t.members from Team t";
//            List<List> result3 = em.createQuery(query,List.class).getResultList();
//            for(List m:result3){
//                System.out.println("m = "+m);
//            }
//            //명시적 조인이면 가능
//            query = "select m from Team t join t.members m";
            //2. 페치 조인
            String query = "select m from Member m join fetch m.team";
            List<Member> result4 = em.createQuery(query,Member.class).getResultList();
            for(Member m:result4){
                /* 페치조인 없을 때
                멤버와 팀의 연관관계에서 지연 로딩으로 설정되어 있으니까,
                나중에 DB에서 조회할 때 가져와서 1차 캐시에 저장한다.
                쿼리가 총 3번 나간다. 멤버 조회, 멤버별 팀 조회
                (member2는 teamA 소속이라 이미 1차캐시에 정보가 있으니까)
                회원 100명이라면? => N+1 문제 발생. 즉시 로딩이든 지연 로딩이든 발생하는 문제
                 */
                /* 페치 조인을 한다면?
                조인을 해서 페치로 한방에 팀을 가져오기 때문에, 쿼리를 한번만 보내면 된다.
                 */
                System.out.println("member = "+m.getName()+" team = "+m.getTeam().getName());
            }
            //컬렉션 페치 조인
            //일대다 조인이라 데이터가 뻥튀기되어 TeamA가 두번 출력된다.
            //Hibernate 5 이하에서는 Team에 Member을 조인하게 되면 teamA의 회원이 두명이라 teamA가 테이블에서 두줄이 되어버린다.
            //Hibernate 6 부터는 알아서 DISTINCT를 삽입하기 때문에 실행해도 두 줄만 출력된다.
            query = "select distinct t from Team t join fetch t.members";;
            List<Team> result5 = em.createQuery(query,Team.class).getResultList();
            for(Team t:result5){
                System.out.println("team = "+t.getName()
                        +" Members = "+t.getMembers().stream().map(Member::getName).toList());
            }
            //엔티티 직접 조회
            query = "select m from Member m where m = :member";
            //query = "select m from Member m where m.id = :memberId";
            Member resultList = em.createQuery(query,Member.class)
                    .setParameter("member",member1)
                            .getSingleResult();
            System.out.println("resultList = "+resultList.getName());
            //네임드 쿼리 사용하기
            //쿼리에 오류가 있으면, 쿼리를 실행하는 시점에 Syntax 에러가 난다.
            //Spring DATA JPA의 @Query와 같은 역할!
            List<Member> result = em.createNamedQuery("Member.findByName",Member.class)
                            .setParameter("username","Member1").getResultList();
            for (Member member : result) {
                System.out.println("member = "+member.getName());
            }
            //벌크 연산
            //모든 회원의 나이를 20살로 바꿔보기
            //쿼리 날리기 전에는 자동으로 플러시되니까 상관 없는데...
            int count = em.createQuery("update Member m set m.age = 20").executeUpdate();
            System.out.println(count); //업데이트된 데이터 수를 리턴한다.
            //여기선 영속성 컨텍스트에 회원 나이가 20살인게 반영이 안되어있다.
            System.out.println(member1.getAge()); // 0으로 출력된다.
            Member findMember = em.find(Member.class,member1.getId());
            System.out.println(findMember.getAge()); //똑같이 0으로 나온다.
            em.clear(); //벌크 연산 수행 후에 영속성 컨텍스트 초기화를 바로 한다면 괜찮다.
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
}
