package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main06_Persist_Context {

    public static void main(String[] args) {

        /* 엔티티 매니저 팩토리 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();       //엔티티 매니저
        EntityTransaction tx = em.getTransaction();         //트랜잭션 받기
        tx.begin();                                         //트랜젝션 시작

        try{
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            Member member = new Member();
            member.setName("member1");
            member.setTeam(team);;
            em.persist(member);

            team.getMembers().add(member);

            em.flush();
            em.clear();

            Member findMember = em.find(Member.class, member.getId());

            //em.close();

            /* 4. 지연로딩과 준영속
            * 준영속 관계로 만들어 버린 후에(em.close() 각주 해제), 지연로딩을 하려고 하면 LazyInitializationException 이 발생한다.
            * 객체를 지연로딩으로 가져오기 위한 근거는 영속성 컨텍스트에 다 있는데, 관계가 끊어져버렸기 때문
            * */
            Team findTeam = findMember.getTeam();
            findTeam.getName();
            System.out.println("findTeam = " + findTeam);
            /* 3. 지연로딩
            * 지연로딩에서는 findTeam에는 가짜 Proxy 객체만 들어있다. */

            /* 2. 준영속 상태
            * em.detach(엔티티 명) : 해당 엔티티만 준영속 상태로 전환.
            * em.clear() : 영속성 컨텍스트를 완전히 초기화
            * em.close() : 영속성 컨텍스트 종료
            * */
            em.detach(findMember);              //em에서 findMember 를 빼버리면, 이후 setName 등 변경을 가할 수 없다.

            findMember.setName("t아카데미");
            /* 1. 이 이후에 findMember 에 대한 UPDATE 쿼리문을 안 써도, commit() 시 JPA 가 자동으로 쿼리를 쳐준다. */

            tx.commit();
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
