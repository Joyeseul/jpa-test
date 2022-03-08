package hellojpa.repository;

import hellojpa.entity.Member;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface MemberRepository extends JpaRepository<Member, Long> {

    Member findByName(String name);
    Page<Member> findByName(String name, Pageable pageable);

    Member findByAge(int i);

//    Page<Member> findByName(String name, Pageable pageable);
//
//    List<Member> findByName(String name);


}
