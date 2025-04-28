package hellojpa.Domain;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Team {
    @Id @GeneratedValue
    private Long id;
    private String name;

    @OneToMany(mappedBy = "team") //뭐랑 연결되어 있지?
    private List<Member> members = new ArrayList<>();

    //이런 방향으로 연관관계 편의 메서드를 생성할 수 있다.
    public void addMember(Member member){
        member.setTeam(this);
        members.add(member);
    }

//    @Override - stackOverFlow 발생 가능
//    public String toString() {
//        return "Team{" +
//                "id=" + id +
//                ", name='" + name + '\'' +
//                ", members=" + members +
//                '}';
//    }
}
