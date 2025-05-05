package jpabook.jpashop.domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.Objects;

@Embeddable
@Getter
@AllArgsConstructor
@NoArgsConstructor
public class Address {
    private String city;
    private String street;
    private String zipcode;

    public String getFullAddress(){
        return city + " " + street + " " + zipcode;
    }

    public boolean isvalid(){
        return city != null && street != null && zipcode != null;
    }

    //equals, hashcode 작성 시 getter 사용하도록 설정 체크하기!
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Address address)) return false;
        return Objects.equals(getCity(), address.getCity()) && Objects.equals(getStreet(), address.getStreet()) && Objects.equals(getZipcode(), address.getZipcode());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getCity(), getStreet(), getZipcode());
    }
}
/*
값 타입을 의미있게 사용하는 방법
- 값 타입을 별도로 빼내고 Value Type으로 빼주면 공유하는 사람들끼리 이해하기 편하니까...

Address는 독립적이지 않고, Member와 Delivery의 일부분으로써 의미가 있기 때문에 여기선 값 타입이 적절하다.
 */