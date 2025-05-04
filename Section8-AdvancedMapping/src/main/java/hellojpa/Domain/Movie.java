package hellojpa.Domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Setter @Getter
@DiscriminatorValue("M") //@DiscriminateColumn에 들어갈 값 변경 가능
public class Movie extends Item {
    private String director;
    private String actor;
}
