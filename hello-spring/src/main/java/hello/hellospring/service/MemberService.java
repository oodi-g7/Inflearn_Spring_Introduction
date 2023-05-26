package hello.hellospring.service;

import hello.hellospring.domain.Member;
import hello.hellospring.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Transactional
public class MemberService {
    // MemberService도 memberRepository가 필요하므로, 해당 객체를 new해서 새로 생성하여 사용하는 것 보단
    // MemberService 생성자를 만들어서 @Autowired 어노테이션을 걸어두면,
    // 스프링 어플리케이션이 실행될때 @Service 어노테이션을 보고 스프링 컨테이너가 MemberService를 컨테이너에 저장하면서 MemberService 생성자를 호출한다.
    // 생성자를 호출하면서 memberRepository가 필요한 것을 보고 컨테이너에 있는 MemberRepository객체를 주입시켜준다.
    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    /**
    * 회원가입
    * */
    public Long join(Member member){

        validateDuplicateMember(member); //중복 회원 검증
        memberRepository.save(member);

        return member.getId();
    }

    //  동일한 이름이 있는 회원은 등록X
    private void validateDuplicateMember(Member member){
        memberRepository.findByName(member.getName())
            .ifPresent(m -> { // ifPresent(변수 -> {}) : m이 null이 아니라 값이 있으면 -> { } 안에 로직 동작. Optional타입이라 사용가능.
                throw new IllegalStateException("이미 존재하는 회원입니다.");
            });
    }

    /**
     * 전체 회원 조회
     * */
    public List<Member> findMembers(){
        return memberRepository.findAll();
    }

    /**
     * 개별 회원 조회
     * */
    public Optional<Member> findOne(Long memberId){
        return memberRepository.findById(memberId);
    }
}
