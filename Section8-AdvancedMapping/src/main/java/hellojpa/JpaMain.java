package hellojpa;

import hellojpa.Domain.Item;
import hellojpa.Domain.Member;
import hellojpa.Domain.Movie;
import hellojpa.Domain.OneToManyExample.Member2;
import hellojpa.Domain.OneToManyExample.Team2;
import hellojpa.Domain.Team;
import jakarta.persistence.*;

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
            //JPA 기본 상속 전략 - 싱글 테이블
            //1. InheritanceType.JOIN
//            Movie movie = new Movie();
//            movie.setDirector("aaaa");
//            movie.setActor("bbbb");
//            //상속 받은거라서 Item에 있는 쿼리에도 INSERT로 같이 들어간다.
//            movie.setName("바람과함께사라지다");
//            movie.setPrice(10000);
//            em.persist(movie);
//            em.flush(); em.clear();
//
//            //INNER JOIN으로 가져온다.
//            Movie findMovie = em.find(Movie.class, movie.getId());
//            System.out.println("findMoive = "+findMovie.getName());
            //InerhitanceType.TABLE_PER_CLASS로 하려면 Item을 abstract class로 만들어야 한다.
            //2. InheritanceType.TABLE_PER_CLASS 단점 예제
//            Movie movie = new Movie();
//            movie.setDirector("aaaa");
//            movie.setActor("bbbb");
//            //상속 받은거라서 Item에 있는 쿼리에도 INSERT로 같이 들어간다.
//            movie.setName("바람과함께사라지다");
//            movie.setPrice(10000);
//            em.persist(movie);
//
//            em.flush(); em.clear();
//
//            Item item = em.find(Item.class, movie.getId());
//            System.out.println("item = "+item.getName());
            /*
            매우 복잡한 쿼리문이 나간다.
Hibernate:
    select
        i1_0.id,
        i1_0.clazz_,
        i1_0.name,
        i1_0.price,
        i1_0.artist,
        i1_0.author,
        i1_0.isbn,
        i1_0.actor,
        i1_0.director
    from
        (select
            price,
            id,
            actor,
            director,
            name,
            null as author,
            null as isbn,
            null as artist,
            1 as clazz_
        from
            Movie
        union
        all select
            price,
            id,
            null as actor,
            null as director,
            name,
            author,
            isbn,
            null as artist,
            2 as clazz_
        from
            Book
        union
        all select
            price,
            id,
            null as actor,
            null as director,
            name,
            null as author,
            null as isbn,
            artist,
            3 as clazz_
        from
            Album
    ) i1_0
where
    i1_0.id=?
             */
            //3. MappedSuperClass
            Member member = new Member();
            member.setCreatedBy("Kim");
            member.setCreatedDate(LocalDateTime.now());
            em.persist(member);

            em.flush(); em.clear();

            tx.commit();
        } catch (Exception e){
            tx.rollback();
        } finally{
            em.close(); //사용 다 하면 꼭 닫기
        }
        //실제로는 스프링이 알아서 다 해주기 때문에 em.persist만 하면 된다.
        emf.close(); //WAS가 내려갈때 emf를 닫아줘야 한다.
    }
}
