package hellojpa.Domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("A") //@DiscriminateColumn에 들어갈 값 변경 가능
public class Album extends Item{

    private String artist;
}
