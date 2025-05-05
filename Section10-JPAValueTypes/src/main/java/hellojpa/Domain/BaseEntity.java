package hellojpa.Domain;

import jakarta.persistence.MappedSuperclass;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

@MappedSuperclass
@Getter
@Setter
public abstract class BaseEntity {
    //모든 테이블에 공통된 정보를 넣어야 할 때!
    //나중엔 spring data jpa와 스프링부트로 자동으로 이 필드에 값을 삽입하는 것이 가능해진다.
    private String createdBy;
    private String lastModifiedBy;
    private LocalDateTime createdDate;
    private LocalDateTime lastModifiedDate;
}
