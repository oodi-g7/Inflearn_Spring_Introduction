package hello.hellospring.repository;

import hello.hellospring.domain.Member;

import java.util.List;
import java.util.Optional;

public interface MemberRepository {
    //멤버추가(저장)
    Member save(Member member); // 멤버 추가(저장)

    //아이디로 멤버찾기
    //optional이란, 자바8에 추가된 기능으로 값이 null인 경우에는 optional로 감싸서 클라이언트에게 전송
    //값이 그냥 null이 아닌 optional로 감싸진 null이므로, 클라이언트는 null값을 받았지만 서비스 종료없이 계속해서 서비스 이용가능.
    Optional<Member> findById(Long id);

    // 이름으로 멤버찾기
    Optional<Member> findByName(String name);

    // 저장된 모든 회원 불러오기
    List<Member> findAll();
}
