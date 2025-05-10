package hellojpa.Domain;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Embeddable
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Period {
    private LocalDateTime startDate;
    private LocalDateTime endDate;

    //응집성 있게 메서드를 만들어 사용할 수 있다.
    public boolean isWorkTime(){
        return startDate.isBefore(LocalDateTime.now()) && endDate.isAfter(LocalDateTime.now());
    }

}
