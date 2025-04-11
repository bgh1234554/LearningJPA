package hellojpa;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import lombok.Getter;
import lombok.Setter;

//만들기 전에 H2 데이터베이스에 SQL로 테이블 생성
@Getter
@Setter
@Entity
public class Member {
    @Id
    private Long id;
    private String name;
}
