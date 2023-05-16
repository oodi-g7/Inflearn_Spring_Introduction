# Section 01

>spring initializr
1. Gradle-Groovy
2. Spring Boot 2.7.11
3. Dependencies : Spring Web, Thymeleaf

>spring boot library
1. spring-boot-starter-web
	spring-boot-starter-tomcat : 톰캣(웹서버)
	spring-webmvc: 스프링 웹 MVC
2. spring-boot-starter-thymeleaf : 타임리프 템플릿 엔진(view)
3. spring-boot-starter(공통) : 스프링부트 + 스프링코어 + 로깅
	spring-boot
		spring-core
	spring-boot-starter-logging
		logback(구현체), slf4j(인터페이스)

>test library
1. spring-boot-starter-test
	junit : 테스트 프레임워크
	mockito : 목 라이브러리
	assertj : 테스트 코드를 좀더 편하게 작성하게끔 도와주는 라이브러리
	spring-test : 스프링 통합 테스트 지원

>Doc 활용
1. spring.io 검색해서 project > springboot > learn 탭선택 > 사용하는버전의 reference doc선택
2. 프로젝트 개발시 도큐먼트 내용 검색할 수 있어야 함 !

>window cmd창에서 프로젝트 빌드하고 실행하기
1. 우선 개발툴에서 실행중인 프로그램 모두 중지시키기
2. cmd창 켜서 프로젝트경로로 이동
3. gradlew.bat 입력
4. gradlew build 입력
5. 프로젝트 build폴더 내에 libs 내 빌드된 jar파일 존재확인
6. 해당 위치에서 java -jar 프로젝트명-SNAPSHOT.jar 입력



# Section 02
>정적컨텐츠

<img src="./image/sec2_static.png">

```
// Controller
@GetMapping("hello")
public String hello(Model model){
	model.addAttribute("data", "hello!!!");
	return "hello";
}

// View
<!DOCTYPE html>
<html>
	<body>
		정적 컨텐츠 입니다.
	</body>
</html>
```

>MVC와 템플릿 엔진 : Thymeleaf

<img src="./image/sec2_mvc,template.png">

```
// Controller
@GetMapping("hello-mvc")
public String helloMvc(@RequestParam("name") String name, Model model){
	model.addAttribute("name", name);
	return "hello-template";
}

// View
<html xmlns:th="http://www.thymeleaf.org">
	<body>
		<p th:text="'hello ' + ${name}">hello! empty</p>
	</body>
</html>
```

>API

<img src="./image/sec2_responsebody.png">

```
// Controller
@GetMapping("hello-string")
@ResponseBody // http프로토콜 body부에 해당 데이터를 직접 입력해주겠다.
public String helloString(@RequestParam("name") String name){
	return "hello " + name;
}

@GetMapping("hello-api")
@ResponseBody
public Hello helloApi(@RequestParam("name") String name) {
	Hello hello = new Hello();
	hello.setName(name);
	return hello;
}

static class Hello {
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
}
```


# Section 03 회원관리예제 - 백엔드 개발
>비즈니스 요구사항 정리   
-	데이터 : 회원ID, 이름
-	기능 : 회원 등록, 조회
-	아직 데이터 저장소가 선정되지 않음(가상의 시나리오)

**일반적인 웹 애플리케이션 계층 구조**

<img src="./image/sec3_1.png">

- 컨트롤러 : 웹 MVC의 컨트롤러 역할
- 서비스 : 핵심 비즈니스 로직 구현 (예)회원은 중복가입이 안된다.
- 리포지토리 : 데이터베이스에 접근, 도메인 객체를 DB에 저장하고 관리
- 도메인 : 비즈니스 도메인 객체 (예)회원, 주문, 쿠폰 등등 주로 DB에 저장하고 관리됨

**클래스 의존관계**

<img src="./image/sec3_2.png">

- 회원 비즈니스 로직에는 회원서비스(memberservice)가 있고, 회원저장소는 interface로 설계한다. 그 이유는 아직 데이터저장소가 선정되지 않았다는 사전조건 때문. 인터페이스로 설계하여 메모리에 저장하는 구현체로 개발을 진행하다가, 구체적인 기술이 선정되고 나면 쉽게 변경가능하도록 인터페이스로 설계
- 아직 데이터 저장소가 선정되지 않아서, 우선 인터페이스로 구현 클래스를 변경할 수 있도록 설계
- 데이터 저장소는 RDB, NoSQL 등등 다양한 저장소를 고민중인 상황으로 가짐
- 개발을 진행하기 위해서 초기 개발 단계에서는 구현체로 가벼운 메모리 기반의 데이터 저장소 사용


>회원 도메인과 리포지토리 만들기   
(*Repository : 회원 도메인 객체를 저장하고 불러올 수 있는 저장소 객체)

>회원 리포지토리 테스트 케이스 작성

>회원 서비스 개발(실제 비즈니스 로직에 있는 회원 서비스)

>회원 서비스 테스트(jUnit)