package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.stereotype.Repository;

import java.util.*;

@Repository
public class MemoryMemberRepository implements MemberRepository {

    // 현재 저장소가 없다는 설정이므로, 등록된 멤버를 저장해둘 변수가 필요함.
    // map변수 store를 static으로 선언하여 등록되는 멤버들을 애플리케이션이 실행되는 동안 갖고 있기로.
    private static Map<Long, Member> store = new HashMap<>();
    private static long sequence = 0L;

    @Override
    public Member save(Member member) {
        member.setId(++sequence); // 새로운 멤버가 들어왔을때, 시퀀스번호를 +1해준 후 해당번호를 id로 설정
        store.put(member.getId(), member); // 멤버의 id와 해당 멤버 객체를 store변수에 담음
        return member;
    }

    @Override
    public Optional<Member> findById(Long id) {
        return Optional.ofNullable(store.get(id)); //만약 해당하는 멤버가 존재하지 않더라도(null이더라도) optional로 감싸서 전송하므로
                                                    //클라이언트는 서비스 중지 없이 계속해서 서비스 이용 가능
    }

    @Override
    public Optional<Member> findByName(String name) {
        // JAVA8 람다 이용
        // store변수 내 값을 루프를 돌면서 member안에 getname이 파라미터로 넘어온 name과 동일한게 있는지 filter 걸어 확인.
        // 해당 방식은 결과가 Optional로 반환이 됨.
        return store.values().stream()
                .filter(member -> member.getName().equals(name))
                .findAny();
    }

    @Override
    public List<Member> findAll() {
        return new ArrayList<>(store.values()); // map에 담긴 내용을 list로 변환하는 방법 !!!!!!!
    }

    public void clearStore(){
        store.clear();
    }
}
