package hello.hellospring.repository;

import hello.hellospring.domain.Member;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.simple.SimpleJdbcInsert;

import javax.sql.DataSource;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class JdbcTemplateMemberRepository implements MemberRepository{

    // jdbc 사용법
    // - jdbc는 DI가 불가능하다. 
    // - 생성자를 통해 datasource를 Injection 받아서 상수로 선언한 jdbcTemplate에 넣어 사용하면 됨.
    private final JdbcTemplate jdbcTemplate;

    @Autowired
    public JdbcTemplateMemberRepository(DataSource dataSource){
        jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Member save(Member member) {
        SimpleJdbcInsert jdbcInsert = new SimpleJdbcInsert(jdbcTemplate); // jdbcTemplate을 넣어서 insert객체를 만듦
        jdbcInsert.withTableName("member").usingGeneratedKeyColumns("id"); // insert객체 설정. 테이블이름 member, pk컬럼은 id

        Map<String, Object> parameters = new HashMap<>(); // values로 입력할 값을 담을 map타입 변수 parameters
        parameters.put("name", member.getName()); // parameters에 메소드 매개값으로 받은 member의 name을 넘겨줌. (컬럼명, 값)

        Number key = jdbcInsert.executeAndReturnKey(new MapSqlParameterSource(parameters)); // insert문 실행 및 해당 row의 pk값 반환
        member.setId(key.longValue()); // save메소드의 매개값으로 받은 member에 id를 전달받은 pk값으로 넣어줌

        return member; // member 객체를 반환 Member{id:pk값, name:전달받은이름};
    }

    @Override
    public Optional<Member> findById(Long id) {
        // jdbcTemplate.query()를 통해 입력한 쿼리문을 실행해서 결과를 가져온 후,
        // 이것을 memberRowMapper()메소드를 호출해서 RowMapper객체를 이용해 Member객체로 변환시켜서 Optional로 return
        List<Member> result = jdbcTemplate.query("select * from member where id = ?", memberRowMapper(), id); // JPQL
        return result.stream().findAny();
    }

    @Override
    public Optional<Member> findByName(String name) {
        List<Member> result = jdbcTemplate.query("select * from member where name = ?", memberRowMapper(), name);
        return result.stream().findAny();
    }

    @Override
    public List<Member> findAll() {
        return jdbcTemplate.query("select * from member", memberRowMapper());
    }

    private RowMapper<Member> memberRowMapper(){
        return new RowMapper<Member>(){ // 익명클래스(내부클래스의 일종), return값은 RowMapper<Member>이다.
            // new 인터페이스명() <- 이것만 보면 RowMapper 인터페이스를 클래스 생성자처럼 초기화해서 인스턴스화 한 것 같지만,
            // 인터페이스는 객체를 만들 수 없으므로 이건 자식 클래스를 생성해서 implements하고 클래스를 초기화한 것과 동일.
            // 익명클래스를 작성함과 동시에 객체를 생성하도록 하는 Java의 문법으로 보면 됨.
            @Override // RowMapper클래스에 있는 mapRow를 재정의
            public Member mapRow(ResultSet rs, int rowNum) throws SQLException{
                Member member = new Member();
                member.setId(rs.getLong("id"));
                member.setName(rs.getString("name"));

                return member;
            }
        };
    }

    // 위 코드를 람다로 바꾸면 아래와 같다.
    private RowMapper<Member> lambda_memberRowMapper(){
        return (rs, rowNum) -> {
            Member member = new Member();
            member.setId(rs.getLong("id"));
            member.setName(rs.getString("name"));
            return member;
        };
    }
}
