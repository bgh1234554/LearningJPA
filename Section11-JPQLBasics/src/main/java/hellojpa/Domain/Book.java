package hellojpa.Domain;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
@DiscriminatorValue("B") //@DiscriminateColumn에 들어갈 값 변경 가능
public class Book extends Item{
    private String author;
    private String isbn;
}
