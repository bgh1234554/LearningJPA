package hellojpa.Domain;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@Setter
@Table(name="ADDRESS")
@NoArgsConstructor
@AllArgsConstructor
public class AddressEntity {

    @Id
    @GeneratedValue
    private Long id;

    private Address address;

    public AddressEntity(String city, String street, String zipcode) {
        address = new Address(city, street, zipcode);
    }
}
