package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import hello.hellospring.repository.MemoryMemberRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
@SpringBootTest
@Transactional
class MemberServiceIntegrationTest {
// 이전에는 객체를 직접 생성해서 넣었으니 해당 메소드가 필요했는데,
// 이제는 스프링 컨테이너한테 service, repository 내놔! 해야하므로 없애야함.
//    MemberService memberService;
//    MemoryMemberRepository memberRepository;
//
//    @BeforeEach
//    public void beforeEach(){
//        memberRepository = new MemoryMemberRepository();
//        memberService = new MemberService(memberRepository);
//    }

    // 위의 코드를 아래로 변경.
    // 의존성 주입방법 중 생성자방식을 가장 권유하지만,
    // 이건 테스트코드이므로 필드주입방식이든 뭐든 편한대로 쓰면 됨
    @Autowired MemberService memberService;
    @Autowired MemberRepository memberRepository;

// 이것도 @Transactional 어노테이션 덕분에 필요없어짐
//    @AfterEach
//    public void afterEach(){
//        memberRepository.clearStore();
//    }

    @Test
    void join() { // 테스트는 한글로 작성가능
        //given
        Member member = new Member();
        member.setName("spring");

        //when
        Long saveId = memberService.join(member);

        //then
        Member findMember = memberService.findOne(saveId).get();
        assertThat(member.getName()).isEqualTo(findMember.getName());
    }

    @Test
    public void 중복_회원_예외(){
        //given
        Member member1 = new Member();
        member1.setName("spring");

        Member member2 = new Member();
        member2.setName("spring");

        //when
        memberService.join(member1);
        //      member2를 넣으면 IllegalState예외가 터져야한다.
        //      예외가 터지면 테스트가 성공이라고 출력됨
        IllegalStateException e = assertThrows(IllegalStateException.class, () -> memberService.join(member1));
        assertThat(e.getMessage()).isEqualTo("이미 존재하는 회원입니다.");

        /*
        try{
            // 예외가 발생해야함
            memberService.join(member2);
            fail();
        }catch(IllegalStateException e){
            assertThat(e.getMessage()) .isEqualTo("이미 존재하는 회원입니다.");
        }
        */

        //then
    }
}