package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.MemberType;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;
import java.util.List;

public class Main {

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
//            member.setId(1L);             //엔터티에서 @GeneratedValue의 전략을 AUTO로 해두면, 자동 시퀀스를 따게 할 수 있다.
            member.setName("member1");
//            member.setTeamId(team.getId());       //JPA를 안 쓸 경우.
            em.persist(member);

            /* 양방향 매핑시 연관관계의 주인에 값을 입력해야 한다. */
            member.setTeam(team);                   //단방향 연관관계 설정, 참조 저장
            team.getMembers().add(member);          //양방향인데, 연관관계 주인으로 넣지 않는 경우임. 이땐 member 의 team이 null로 들어간다


            /* 캐싱없이 쿼리문이 나가는 걸 직접 보고 싶을 때 사용 */
            em.flush();                             //db에 쿼리보냄
            em.clear();                             //캐시를 깨끗하게 지움

            /* 기존에 이 회원의 팀을 조회하려면 두 단계를 거쳐야 했다.
            * 1. 멤버의 teamId 찾기
            * 2. teamId로 team 찾기 */
            Member findMember = em.find(Member.class, member.getId());
//            Long teamId = findMember.getTeamId();                 //teamId를 받아야 함
//            Team findTeam = em.find(Team.class, teamId);          //member 와 team은 연관관계가 없음

            /* ORM 매핑을 사용하면 바로 team 객체를 가져올 수 있다. */
            Team team1 = findMember.getTeam();
            Team findTeam = findMember.getTeam();                   //참조를 사용해서 연관관계 조회

            /* fetchType.LAZY : 해당객체를 정말 조회할 시점에 객체를 가져온다.
            * fetchType.EAGER : 연관 객체를 조회할 때 같이 JOIN으로 가져온다.
            * */
            String findTeamName = findTeam.getName();

            /* 새로운 팀 B를 만들고, 팀을 변경할 때는 그냥 set 사용 */
            Team teamB = new Team();
            teamB.setName("TeamB");
            em.persist(teamB);
            member.setTeam(teamB);

            /* 역방향 조회도 가능하다. */
//            List<Member> members = findTeam.getMembers();
//            for(Member member1 : members) {
//                System.out.println("member1 = " + member1);
//            }


            tx.commit();
        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }
        
        System.out.println("hello");
        emf.close();                                    //웹프로젝트를 아예 내릴 때
    }
}
