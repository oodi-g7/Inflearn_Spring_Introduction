package hello.hellospring.controller;

import hello.hellospring.service.MemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;

@Controller
public class MemberController {
    // MemberController에서 MemberService를 사용해야하는데, 만약 private final MemberService memberSerivce = new MemberService();
    // 이런식으로 service객체를 새로 만들어 사용하는 것보다는
    // 아래와같이 memberService를 상수로 선언해주고,
    // MemberController 생성자를 만들어서 @Autowired를 걸어주면 spring컨테이너에 등록된 service객체가 주입되어
    // MemberController 내에서 사용될 수 있다. 이것이 바로 dependency injection 의존성주입 !
    private final MemberService memberService;

    @Autowired
    public MemberController(MemberService memberService){
        this.memberService = memberService;
    }
}
