package hellojpa;

import hellojpa.Domain.*;
import hellojpa.Domain.Cascade.Child;
import hellojpa.Domain.Cascade.Parent;
import hellojpa.Domain.OneToManyExample.Member2;
import hellojpa.Domain.OneToManyExample.Team2;
import jakarta.persistence.*;
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

        try{
            //1. Embedded 활용 예제
//            Member member = new Member();
//            member.setName("hello");
//            member.setHomeAddress(new Address("city", "street", "zipcode"));
//            member.setWorkPeriod(new Period(LocalDateTime.now(), LocalDateTime.now().plusDays(1)));
//            em.persist(member);

            //2. 값 타입과 불변 객체
//            Address address = new Address("city", "street", "zipcode");
//            Member member = new Member();
//            member.setName("member1");
//            member.setHomeAddress(address);
//            em.persist(member);
//
//            Member member2 = new Member();
//            member2.setName("member2");
//            member2.setHomeAddress(address);
//            em.persist(member2);
//
//            //둘 다 같은 주소를 쓰면???
//
//            //세터 없이 불변 객체로 만들면 애초에 이 문제를 원천 차단할 수 있다.
//            member.getHomeAddress().setCity("newCity"); //첫번째 멤버만 바꾸고 싶어서 이렇게 했는데...
//            //UPDATE 쿼리가 두 번 나간다!!! -> 실무에서 진짜 잡기 어려운 버그
//
//            //세터 없애면 원래 하고 싶던 멤버1에 대한 주소 변경은 어떻게 하는데??
//            //그냥 새롭게 주소 객체를 만들어야 한다...
//            Address newAddress = new Address("newCity", address.getStreet(), address.getZipcode());
//            member.setHomeAddress(newAddress);
//            em.persist(member);

            //3. 값 타입의 비교
//            int a = 10; int b = 10;
//            System.out.println(a==b); //true
//            Address address1 = new Address("city", "street", "zipcode");
//            Address address2 = new Address("city", "street", "zipcode");
//            //동일성 비교 - 참조 값을 비교
//            System.out.println(address1==address2); //false
//            //동등성 비교 - 값으로 비교하려면 equals 메서드 사용
//            System.out.println(address1.equals(address2));
//            //기본 equals는 ==이라 false가 나오기 때문에 재정의가 필요하다.
//            //그냥 기본으로 생성해서 제공되는 equals 메서드를 사용해야 한다. -> 해시 맵도 알아서 재정의해주니까.

            //4. 값 타입 컬렉션
            Member member = new Member();
            member.setName("member1");
            member.setHomeAddress(new Address("homeCity", "street", "zipcode"));

            member.getFavoriteFoods().add("chicken");
            member.getFavoriteFoods().add("beef");
            member.getFavoriteFoods().add("pizza");

            member.getAddressHistory().add(new AddressEntity("city1", "street1", "zipcode1"));
            member.getAddressHistory().add(new AddressEntity("city2", "street2", "zipcode2"));

            em.persist(member); //따로 persist하지 않아도, 한번에 3개의 테이블에 INSERT 쿼리가 날라간다.
            //값 타입 컬렉션도 본인의 생성주기가 따로 없기 때문이다.

            em.flush(); em.clear();

            Member findMember = em.find(Member.class, member.getId());

            //지연 로딩이라 조회할 때 DB에서 가지고 온다.
            //값 타입 조회 예제
            List<AddressEntity> addressHistory = findMember.getAddressHistory();
            for (AddressEntity address : addressHistory) {
                System.out.println("address = " + address.getAddress().getCity());
            }
            Set<String> favoriteFoods = findMember.getFavoriteFoods();
            for (String favoriteFood : favoriteFoods) {
                System.out.println("favoriteFood = " + favoriteFood);
            }

            //값 타입 수정 예제 - 값 타입은 불변 객체기 떄문에 세터로 변경하면 안된다.
            findMember.setHomeAddress(new Address("newCity", "newStreet", "newZipcode"));
            //이런식으로 새로운 객체로 갈아껴줘야 한다.

            //값 타입 컬렉션 업데이트

            //치킨 -> 한식 (String은 업데이트라는 것 자체가 안되니까 갈아껴야 한다)
            findMember.getFavoriteFoods().remove("chicken");
            findMember.getFavoriteFoods().add("korean");

            //이래서 equals와 hash 메서드가 중요해진다.
            //그냥 기존 테이블을 통으로 지워버리고, 데이터를 새롭게 새 테이블에 INSERT 한다.
            findMember.getAddressHistory().remove(new AddressEntity("city1", "street1", "zipcode1"));
            findMember.getAddressHistory().add(new AddressEntity("newCity1", "newStreet1", "newZipcode1"));

            em.flush(); em.clear();
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
