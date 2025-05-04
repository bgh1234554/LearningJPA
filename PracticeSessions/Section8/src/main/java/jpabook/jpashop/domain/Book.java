package jpabook.jpashop.domain;

import jakarta.persistence.Entity;
import lombok.Getter;
import lombok.Setter;

@Entity
@Getter
@Setter
public class Book extends Item {
    private String isbn;
    private String author;
}
