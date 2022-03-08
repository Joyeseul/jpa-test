package hellojpa;

import com.mysema.query.BooleanBuilder;
import com.querydsl.jpa.impl.JPAQueryFactory;
import hellojpa.entity.Member;
import hellojpa.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.web.bind.annotation.*;

import javax.annotation.PostConstruct;
import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;
import java.util.List;

@RestController
public class SampleController {

    @Autowired
    MemberRepository repository;

    @PostConstruct
    public void init() {
        repository.save(new Member("hello1", 1));
        repository.save(new Member("hello2", 2));
        repository.save(new Member("hello3", 3));
    }

    @GetMapping("/hello")
    public Page<Member> member() {
        PageRequest request = PageRequest.of(0, 10);
        return repository.findByName("hello1", request);
    }

    @RequestMapping("/members")
    List<Member> members() {
        return repository.findAll();
    }

    @RequestMapping("/members/{memberId}")
    Member member(@PathVariable("memberId") Member member) { return member; }

    @RequestMapping("/search1")
    Page<Member> search(@RequestParam("name") String name, Pageable pageable) {
        return repository.findByName(name, pageable);
    }

    @RequestMapping("/search2")
    Page<Member> logic(@RequestParam("name") String name, Pageable pageable) {
        Member m = new Member();
        repository.save(m);
        return repository.findByName(name, pageable);
    }

    @PersistenceContext
    EntityManager em;

    public void helloQueryDSL() {
        //기존 JPQL
        //String jpql = "SELECT m FROM MEMBER m WHERE m.age > 18"

        /* JPAQueryFacotyr를 생성하고,
        * 객체와 상응되는 Q객체(쿼리 타입용 쿼리 클래스)를 생성해서 이용한다. (추가 설정 필요)
        * */
        JPAQueryFactory query = new JPAQueryFactory(em);
        QMember m = QMember.member;

        /* QueryDSL 이 java Code 들을 SQL 로 변환해준다.
        * 장점 1. IDE 자동완성의 도움
        * 장점 2. 컴파일 시점에 오류를 잡을 수 있다.
        * */
        List<Member> hello = query
                .selectFrom(m)
                .where(m.age.gt(18).and(m.name.contains("hello")))
                .orderBy(m.age.desc())
                .limit(0)
                .offset(10)
                .fetch();

        for(Member member : hello) {
            System.out.println("member = " + member);
        }

        /* 동적 쿼리도 가능 */
//        String name = "member";
//        int age = 9;
//
//        QMember m = QMember.member;
//
//        BooleanBuilder builder = new BooleanBuilder();
//        if(name != null) {
//            builder.and(m.name.contains(name));
//        }
//        if(age != 0) {
//            builder.and(m.age.gt(age));
//        }
//
//        List<Member> list =
//                query.selectFrom(m)
//                        .where(builder)
//                        .fetch();

    }
}
