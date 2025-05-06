package hellojpa;

import hellojpa.Domain.*;
import hellojpa.Domain.Cascade.Child;
import hellojpa.Domain.Cascade.Parent;
import hellojpa.Domain.OneToManyExample.Member2;
import hellojpa.Domain.OneToManyExample.Team2;
import jakarta.persistence.*;
import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Root;
import org.hibernate.Hibernate;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Set;

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
            //1. JPQL 소개
//            Member member = new Member();
//            member.setName("kim");
//            member.setAge(10);
//            em.persist(member);
            //JPQL은 alias를 SQL의 * 대신 사용 가능하다...
//            em.createQuery("select m from Member m where m.name like '%kim%'", Member.class).getResultList();
//            //Criteria 사용 준비
//            CriteriaBuilder cb = em.getCriteriaBuilder();
//            CriteriaQuery<Member> query = cb.createQuery(Member.class);
//            //루트 클래스 (조회를 시작할 클래스)
//            Root<Member> m = query.from(Member.class);
//            //쿼리 생성
//            CriteriaQuery<Member> cq = query.select(m).where(cb.equal(m.get("name"), "kim"));
//            List<Member> resultList = em.createQuery(cq).getResultList();
//
//            //그냥 생 쿼리 날리기
//            em.createNativeQuery("select * from member where username = 'kim'").getResultList();

            //2. 본격적인 JPQL 시작
            //TypeQuery vs Query
            //두번째 인수는 타입 정보 (기본적으로 엔티티)
            //TypedQuery로 반환 타입을 지정할 수 있다.
//            TypedQuery<Member> query1 = em.createQuery("select m from Member m", Member.class);
//            TypedQuery<String> query2 = em.createQuery("select m from Member m", String.class);
//            Query query3 = em.createQuery("select m from Member m"); //반환 타입이 명확하지 않을 때 사용
//
//            List<Member> query4 = em.createQuery("select m from Member m", Member.class).getResultList();
//            //결과가 정확히 하나가 나오지 않으면 예외가 터진다!
//            //-> Spring DATA JPA에서는 null이나 Optional을 반환해줄 수 있도록 코드가 짜져 있다.
//            Member query5 = em.createQuery("select m from Member m where m.id=1", Member.class).getSingleResult();
//
//            //파라미터 설정
// //            TypedQuery<Member> query6 = em.createQuery("select m from Member m where m.name = :username", Member.class);
// //            query6.setParameter("username", "kim");
// //            Member query7 = query6.getSingleResult();
//            //실제론 이렇게 더 많이 사용
//            Member query7 = em.createQuery("select m from Member m where m.name = :username", Member.class)
//                .setParameter("username", "kim")
//                .getSingleResult();
//            System.out.println(query7.getName());

            //3. 프로젝션
//            em.flush(); em.clear();
//            List<Member> findMember = em.createQuery("select m from Member m").getResultList();
//            findMember.get(0).setAge(100); //변경 된다. -> 엔티티 프로젝션을 하면 나온 결과도 영속성 컨텍스트에서 계속 관리한다.
//            //알아서 조인하는 쿼리가 나간다.
//            List<Team> result = em.createQuery("select m.team from Member m", Team.class)
//                    .getResultList();
//            //임베디드 타입 프로젝션 -> 어느 엔티티 소속인지 지정하고, 거기서 시작해야 한다.
//            em.createQuery("select o.address from Order o",Address.class)
//                            .getResultList();
//            //막 가져오는 것 -> 스칼라 프로젝션
//            //제네릭으로 Object[]로 받을 수 있다.
//            List<Object[]> resultList = em.createQuery("select distinct m.name, m.age from Member m")
//                    .getResultList();
//
//            Object[] result2 = resultList.get(0);
//            System.out.println(result2[0]+" "+result2[1]);
//
//            //new 명령어로 DTO로 받는 것
//            //생성자를 통해서 객체가 만들어진다.
//            //단점 - 패키지가 길어져도 그 경로를 다 적어야 한다.
//            List<MemberDTO> resultDTOs = em.createQuery("select new hellojpa.Domain.MemberDTO(m.name, m.age) from Member m", MemberDTO.class)
//                    .getResultList();
//            MemberDTO resultDTO = resultDTOs.get(0);
//            System.out.println(resultDTO.getName()+" "+resultDTO.getAge());

            //4. 페이징 API
//            for (int i = 0; i < 100; i++){
//                Member member = new Member();
//                member.setName("member"+i);
//                member.setAge(i);
//                em.persist(member);
//            }
//            List<Member> resultList = em.createQuery("select m from Member m order by m.age desc", Member.class)
//                    .setFirstResult(1).setMaxResults(10).getResultList();
//
//            for (Member member : resultList){
//                printMember(member);
//            }

            //5. 조인 연산 예제
//            Team team = new Team();
//            team.setName("teamA");
//            em.persist(team);
//
//            Member member = new Member();
//            member.setName("member1");
//            member.setAge(10);
//            member.setTeam(team);
//            em.persist(member);
//
//            em.flush(); em.clear();
//
//            String query = "select m from Member m inner join m.team t where t.name = :teamName";
//            List<Member> resultList = em.createQuery(query, Member.class)
//                    .setParameter("teamName", "teamA")
//                    .getResultList();
//            //세타 조인 - CROSS JOIN 쿼리가 나간다.
//            query = "select m from Member m, Team t where m.name = t.name";
//            resultList = em.createQuery(query, Member.class).getResultList();
//            //ON 절 연습
//            //1. 조인 대상 필터링
//            query = "select m from Member m left join m.team t on t.name = :teamName";
//            resultList = em.createQuery(query,Member.class)
//                    .setParameter("teamName","teamA").getResultList();
//            //2. 연관관계 없는 엔티티 외부 조인
//            query = "select m from Member m left join Team t on m.name = t.name";
//            resultList = em.createQuery(query,Member.class).getResultList();

            //6. 서브쿼리 예제
//            Member member = new Member();
//            member.setName("member1");
//            member.setAge(10);
//            member.setMemberType(MemberType.USER);
//
//            em.persist(member);
//            em.flush(); em.clear();
//
//            String query = "select m.name, 'HELLO', true from Member m " +
//                    "where m.memberType = :userType";
//            List<Object[]> resultList = em.createQuery(query)
//                    .setParameter("userType",MemberType.USER).getResultList();
//            System.out.println("========");
//            for (Object[] m : resultList){
//                System.out.println(m[0]);
//                System.out.println(m[1]);
//                System.out.println(m[2]);
//            }
//            System.out.println("========");
            //7. 조건식
//            Member member = new Member();
//            member.setName("member1");
//            member.setAge(10);
//            member.setMemberType(MemberType.USER);
//            em.persist(member);
//
//            //CASE
//            //나중에 QueryDSL을 배우면 자바 코드로 이런 SQL 문을 쉽게 작성할 수 있다.
//            String query = "select "+
//                        "case when m.age <=10 then '학생요금' "+
//                        "when m.age >=60 then '경로요금' "+
//                        "else '일반요금' end "+
//                    "from Member m";
//            List<String> resultList = em.createQuery(query, String.class).getResultList();
//            for(String s : resultList){
//                System.out.println("s = "+s);
//            }
//            //COALESCE
//            Member member2 = new Member();
//            member2.setAge(20);
//            member2.setMemberType(MemberType.USER);
//            em.persist(member2);
//            query = "select coalesce(m.name,'이름 없음') from Member m";
//            resultList = em.createQuery(query, String.class).getResultList();
//            for(String s : resultList){
//                System.out.println("s = "+s);
//            }
//            //NULLIF
//            //이름이 member1이면 NULL을 반환한다.
//            query = "select nullif(m.name,'member1') from Member m";
//            resultList = em.createQuery(query, String.class).getResultList();
//            for(String s : resultList){
//                System.out.println("s = "+s);
//            }
            //8. JPQL 기본 함수 + 사용자 정의 함수
            Member member = new Member();
            member.setName("member1");
            member.setAge(10);
            member.setMemberType(MemberType.USER);
            em.persist(member);
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
        System.out.println("member = "+member.getName()+" "+member.getAge());
    }

    private static void printMemberAndTeam(Member member) {
        String username = member.getName();
        Team team = member.getTeam();
        System.out.println("username = " + username);
        System.out.println("team = " + team);
    }
}
