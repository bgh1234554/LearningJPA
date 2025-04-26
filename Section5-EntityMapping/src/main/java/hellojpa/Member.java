package hellojpa;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.annotation.processing.Generated;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.Date;

//만들기 전에 H2 데이터베이스에 SQL로 테이블 생성
@Getter
@Setter
@AllArgsConstructor
@Entity //JPA가 관리하는 객체 - DB 테이블과 매핑
@NoArgsConstructor
//@Table(name="MBR") // MBR이라는 테이블과 매핑하도록 지정할 수 있다.
@SequenceGenerator(name="memberSeq", sequenceName="MBR_SEQ", initialValue = 1, allocationSize = 1)
//@TableGenerator(
//        name = "MEMBER_SEQ_GENERATOR",
//        table = "MY_SEQUENCES", //실행시 MY_SEQUNECES라는 테이블이 생성된다.
//        pkColumnValue = "MEMBER_SEQ", allocationSize = 1)
public class Member {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "memberSeq")
//    @GeneratedValue(strategy = GenerationType.TABLE,
//            generator = "MEMBER_SEQ_GENERATOR")
    private Long id;
    /*
    • IDENTITY: 데이터베이스에 위임, MYSQL
    • SEQUENCE: 데이터베이스 시퀀스 오브젝트 사용, ORACLE
    • @SequenceGenerator 필요
    • TABLE: 키 생성용 테이블 사용, 모든 DB에서 사용
    • @TableGenerator 필요
    • AUTO: 방언에 따라 자동 지정, 기본값
     */

    //@Column(unique = true, length = 10) - DDL 생성을 도와준다.
    @Column(name="name", nullable = false) //이런식으로 커스터마이징 할 수 있다.
    //nullable = false -> SQL의 not null 제약 조건임.
    //@Column(updatable = false) //이러면 절대 변경되지 않는다. DB에 직접 들어가서 변경하지 않는 이상, JPA로는.
    private String username;
    //객체에는 username이라고 쓰고, DB에는 name이라고 쓰고 싶은 경우.

    private Integer age;

    @Enumerated(EnumType.STRING)
    private RoleType roleType;

    @Temporal(TemporalType.TIMESTAMP)
    //TemporalType에는 3가지가 있는데, DATE (날짜만), TIME (시간만), TIMESTAMP (둘다 있는 것)
    private Date createdDate;

    @Temporal(TemporalType.TIMESTAMP)
    private Date lastModifiedDate;

    private LocalDate testLocalDate; //년월
    private LocalDateTime testLocalDateTime; //년월일
    //Hibernate는 어노테이션 없이도 타입을 보면 아니까, 알아서 DB의 date와 timestamp 타입으로 생성해준다.
    //과거버전을 써야하면 위의 @Temporal 어노테이션이 필요하다.

    @Lob //varchar을 넘어서는 큰 컨텐츠를 넣고 싶을 때
    //Lob은 속성이 없다. 매핑하는 필드 타입이 문자면 CLOB, 나머지면 BLOB으로 매핑된다.
    private String description;

    @Transient
    private int tmp; //DB와 관계없는 필드일 때 DB에 등록하고 싶지 않을 때 @Transient 사용

    //JPA는 동적으로 객체를 생성해야 해서 기본 생성자를 만들어 놔야 다른 생성자를 만들 수 있다.
    //@AllArgsConstructor 부터 하면 오류가 나서 @NoArgsConstructor을 먼저 붙여야 한다.

}
