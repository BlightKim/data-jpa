package study.datajpa.entity;

import static javax.persistence.FetchType.*;

import java.util.Objects;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@ToString(of = {"id", "username", "age"})
@NamedQuery(
    name = "Member.findByUsername",
    query = "select m from Member m where m.username = :username"
)
public class Member {

  @Id
  @GeneratedValue
  @Column(name = "member_id")
  private Long id;

  private String username;

  private int age;

  @ManyToOne(fetch = LAZY)
  @JoinColumn(name = "team_id")
  private Team team;

  public Member(String username, int age, Team team) {
    this.username = username;
    this.age = age;
    if (team != null) {
      changeTeam(team);
    }
  }

  public Member(String username) {
    this.username = username;
  }

  public Member(String username, int age) {
    this.username = username;
    this.age = age;
  }

  public void changeTeam(Team team) {
    this.team = team;
    team.getMembers().add(this);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    Member member = (Member) o;
    return getAge() == member.getAge() && Objects.equals(getId(), member.getId())
        && Objects.equals(getUsername(), member.getUsername()) && Objects.equals(
        getTeam(), member.getTeam());
  }

  @Override
  public int hashCode() {
    return Objects.hash(getId(), getUsername(), getAge(), getTeam());
  }
}
