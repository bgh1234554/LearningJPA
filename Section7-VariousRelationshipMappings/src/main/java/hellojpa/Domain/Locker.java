package hellojpa.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Locker {

    @Id @GeneratedValue
    private Long id;

    private String name;

    //일대일 양방향 관계 만들어보기
    @OneToOne(mappedBy = "locker")
    private Member member;
}
