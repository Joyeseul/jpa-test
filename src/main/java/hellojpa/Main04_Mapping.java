package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main04_Mapping {
    public static void main(String[] args) {

        /* 엔티티 매니저 팩토리 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();       //엔티티 매니저
        EntityTransaction tx = em.getTransaction();         //트랜잭션 받기
        tx.begin();                                         //트랜젝션 시작

        try{
            /* 팀 저장 */
            Team team = new Team();
            team.setName("teamA");
            em.persist(team);

            /* 회원 등록 */
            Member member = new Member();
            member.setName("hello");
            em.persist(member);

            /* 객체끼리 mapping 을 시켜줄 수 있다. */
            //member.setTeamId(team.getId());       //기존방식. JPA를 안 쓸 경우.
            member.setTeam(team);                   //단방향 연관관계 설정, 참조 저장

            /* 캐싱없이 쿼리문이 나가는 걸 직접 보고 싶을 때 사용 */
            em.flush();                             //db에 쿼리보냄
            em.clear();                             //캐시를 깨끗하게 지움

            /* 기존에 이 회원의 팀을 조회하려면 두 단계를 거쳐야 했다.
             * 1. 멤버의 teamId 찾기
             * 2. teamId로 team 찾기 */
            Member findMember = em.find(Member.class, member.getId());
//            Long teamId = findMember.getTeamId();                 //기존방식. teamId를 받아야 함
//            Team findTeam = em.find(Team.class, teamId);          //기존방식. member 와 team은 연관관계가 없음

            /* ORM 매핑을 사용하면 바로 team 객체를 가져올 수 있다. */
            Team findTeam = findMember.getTeam();                   //이젠 참조를 사용해서 연관관계 조회

            /* fetchType.LAZY : 해당객체를 정말 조회할 시점에 객체를 가져온다.
             * fetchType.EAGER : 연관 객체를 조회할 때 같이 JOIN으로 가져온다.
             * */

            /* 역방향 조회도 가능하다. */
            List<Member> members = findTeam.getMembers();
            for(Member member1 : members) {
                System.out.println("member1 = " + member1);
            }

            /* 연관관계 수정
            * 새로운 팀 B를 만들고, 팀을 변경할 때는 그냥 set 사용
            * */
            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);
            member.setTeam(teamB);

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
