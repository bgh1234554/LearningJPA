package hellojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

//만들기 전에 H2 데이터베이스에 SQL로 테이블 생성
@Getter
@Setter
@AllArgsConstructor
@Entity
@NoArgsConstructor
public class Member {
    @Id
    private Long id;
    private String name;

    //JPA는 동적으로 객체를 생성해야 해서 기본 생성자를 만들어 놔야 다른 생성자를 만들 수 있다.
    //@AllArgsConstructor 부터 하면 오류가 나서 @NoArgsConstructor을 먼저 붙여야 한다.

}
