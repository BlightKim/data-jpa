package study.datajpa.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.entity.Member;

@SpringBootTest
@Transactional
@Slf4j
class MemberJpaRepositoryTest {
  @Autowired
  MemberJpaRepository memberJpaRepository;

  @Test
  void findByUsernameAndAgeGreaterThan() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    List<Member> list = memberJpaRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
    log.info("list = {}", list);
    assertThat(list.get(0).getUsername()).isEqualTo("AAA", 15);
    assertThat(list.get(0).getAge()).isEqualTo(20);

  }

  @Test
  public void testNamedQuery() {

    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);

    memberJpaRepository.save(member1);
    memberJpaRepository.save(member2);

    List<Member> list = memberJpaRepository.findByUsername(member1.getUsername());

    assertThat(list.get(0).getUsername()).isEqualTo("AAA");

  }

  @Test
  public void paging() {
    memberJpaRepository.save(new Member("member1", 10));
    memberJpaRepository.save(new Member("member2", 10));
    memberJpaRepository.save(new Member("member3", 10));
    memberJpaRepository.save(new Member("member4", 10));
    memberJpaRepository.save(new Member("member5", 10));
    memberJpaRepository.save(new Member("member6", 10));
    memberJpaRepository.save(new Member("member7", 10));
    memberJpaRepository.save(new Member("member8", 10));
    memberJpaRepository.save(new Member("member9", 10));
    memberJpaRepository.save(new Member("member10", 10));

    int age = 10;
    int offset = 0;
    int limit = 3;
    List<Member> members = memberJpaRepository.findByPage(age, offset, limit);
    Long totalCount = memberJpaRepository.totalCount(age);

    assertThat(members.size()).isEqualTo(3);
    assertThat(totalCount).isEqualTo(10);
  }
}