package study.datajpa.repository;


import static org.assertj.core.api.Assertions.assertThat;

import java.lang.reflect.Array;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.test.annotation.Rollback;
import org.springframework.transaction.annotation.Transactional;
import study.datajpa.dto.MemberDto;
import study.datajpa.entity.Member;
import study.datajpa.entity.Team;

@SpringBootTest
@Transactional
@Rollback(value = false)
@Slf4j
class MemberRepositoryTest {
  @Autowired MemberRepository memberRepository;
  @Autowired TeamRepository teamRepository;

/*  @Test
  public void testMember() {

    Member member = new Member("memberA", 10, teamA);
    Member savedMember = memberRepository.save(member);

    Member findMember = memberRepository.findById(savedMember.getId()).get();

    assertThat(findMember.getId()).isEqualTo(member.getId());
    assertThat(findMember.getUsername()).isEqualTo(member.getUsername());
    assertThat(findMember).isEqualTo(member);
  }*/
  
  
  @Test
  public void basicCRUD() {
    log.info("memberRepository={}", memberRepository.getClass());
    Member member1 = new Member("member1");
    Member member2 = new Member("member2");
    memberRepository.save(member1);
    memberRepository.save(member2);

    Member findMember1 = memberRepository.findById(member1.getId()).get();
    Member findMember2 = memberRepository.findById(member2.getId()).get();
    assertThat(findMember1).isEqualTo(member1);
    assertThat(findMember2).isEqualTo(member2);

    List<Member> all = memberRepository.findAll();
    assertThat(all.size()).isEqualTo(2);

    long count = memberRepository.count();
    assertThat(count).isEqualTo(2);

    memberRepository.delete(member1);
    memberRepository.delete(member2);

    long deletedCount = memberRepository.count();
    assertThat(deletedCount).isEqualTo(0);
  }

  @Test
  void findByUsernameAndAgeGreaterThan() {
    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("AAA", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> list = memberRepository.findByUsernameAndAgeGreaterThan("AAA", 15);
    log.info("list = {}", list);
    assertThat(list.get(0).getUsername()).isEqualTo("AAA", 15);
    assertThat(list.get(0).getAge()).isEqualTo(20);

  }

  @Test
  void findHelloBy() {
    memberRepository.findTop3HelloBy();
  }

  @Test
  public void namedQuery() {


    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> list = memberRepository.findByUsername(member1.getUsername());

    assertThat(list.get(0).getUsername()).isEqualTo("AAA");

  }

  @Test
  public void testQuery() {


    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> list = memberRepository.findUser("AAA", 10);
    assertThat(list.get(0)).isEqualTo(member1);

  }

  @Test
  public void findUsernameList() {


    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<String> list = memberRepository.findUsernameList();
    assertThat(list.size()).isEqualTo(2);
    assertThat(list).contains("AAA", "BBB");

  }

  @Test
  public void findMemberDto() {
    Team team = new Team("teamA");
    teamRepository.save(team);


    Member member1 = new Member("AAA", 10);
    member1.setTeam(team);
    memberRepository.save(member1);

    List<MemberDto> list = memberRepository.findMemberDto();

    log.info("list = {}", list);

  }

  @Test
  public void findByUsernames() {


    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    List<Member> list = memberRepository.findByNames(Arrays.asList("AAA", "BBB"));
    assertThat(list.size()).isEqualTo(2);
    assertThat(list).contains(member1, member2);

  }
  @Test
  public void returnType() {


    Member member1 = new Member("AAA", 10);
    Member member2 = new Member("BBB", 20);

    memberRepository.save(member1);
    memberRepository.save(member2);

    Optional<Member> result = memberRepository.findOptionalByUsername("AAA");
    log.info("result={}",result);

  }

  @Test
  public void paging() {
    memberRepository.save(new Member("member1", 10));
    memberRepository.save(new Member("member2", 10));
    memberRepository.save(new Member("member3", 10));
    memberRepository.save(new Member("member4", 10));
    memberRepository.save(new Member("member5", 10));
    memberRepository.save(new Member("member6", 10));
    memberRepository.save(new Member("member7", 10));
    memberRepository.save(new Member("member8", 10));
    memberRepository.save(new Member("member9", 10));
    memberRepository.save(new Member("member10", 10));

    int age = 10;
    PageRequest pageRequest = PageRequest.of(0, 3, Sort.by(Direction.DESC, "username"));

    Page<Member> page = memberRepository.findByAge(age, pageRequest);
    List<Member> content = page.getContent();
    log.info("content={}", content);

    assertThat(content.size()).isEqualTo(3);
    assertThat(page.getTotalElements()).isEqualTo(10);
    assertThat(page.getNumber()).isEqualTo(0);
    assertThat(page.getTotalPages()).isEqualTo(4);
    assertThat(page.isFirst()).isTrue();
    assertThat(page.hasNext()).isTrue();
  }
}