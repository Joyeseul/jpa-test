package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main07_JPQL {

    public static void main(String[] args) {

        /* 엔티티 매니저 팩토리 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();       //엔티티 매니저
        EntityTransaction tx = em.getTransaction();         //트랜잭션 받기
        tx.begin();                                         //트랜젝션 시작

        try {
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("hello");
            member.setTeam(team);
            em.persist(member);
            team.getMembers().add(member);

            /* JPQL 검색.
            * Member 객체 단위로 list 를 받아옴. 조건도 where 절에 걸 수 있다.
            * */
            String jpql1 = "select m From Member m where m.name like '%hello%'";
            List<Member> result = em.createQuery(jpql1, Member.class).getResultList();

            /* Fetch Join
            * 만일 Lazy Loading 사용 한다면, memb.getTeam() 을 할 때마다 로딩을 해와야 함. N + 1 문제가 발생한다.
            * fetch 로 이 문제를 해결할 수 있다.
            * */
            String jpqlFetch = "select m from Member m join fetch m.team";
            List<Member> members = em.createQuery(jpqlFetch, Member.class).getResultList();

            for(Member memb : members) {
                //페치 조인으로 회원과 팀을 함께 조회해서, 지연 로딩 발생 안 함
                System.out.println("username = " + memb.getName() + ", "
                        + "teamname = " + memb.getTeam().getName());
            };

            /* 페이징 API
            * MySQL : limit ? offset ?
            * Oracle : 3단 depth 로 ROWNUM
            * */
            List<Member> resultPagingList = em.createQuery(jpqlFetch, Member.class)
                    .setFirstResult(10)
                    .setMaxResults(20)
                    .getResultList();

        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        System.out.println("hello");
        emf.close();                                    //웹프로젝트를 아예 내릴 때
    }
}
