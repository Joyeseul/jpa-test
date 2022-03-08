package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.Team;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main05_mappedBy {

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
            member.setName("member1");
            em.persist(member);
            
            /* 양방향 매핑
            * 하나의 MEMBER 테이블을, Member 객체도 Team 객체도 건들 수 있다.
            * 따라서 둘 중 하나만 주인을 정하고, 나머지 하나는 조회만 가능하게 하자.
            * 연관 관계의 주인만이 외래키를 관리(등록, 수정)할 수 있고, 아닌 쪽(mappedBy)은 읽기만 가능하다.
            * */

            /* 연관관계의 주인
            * 양방향 매핑 시, '연관관계의 주인'에 값을 입력해야 한다.
            * 그럼 주인을 누구로 해야 하나? '외래키'가 있는 곳을 주인으로 정해라.
            * 설계할 땐 다 단방향으로 하고, 나중에 team 에서 member 를 조회해야 한다면 그때 양방향을 뚫으면 된다.
            * */
            member.setTeam(team);                   //단방향 연관관계 설정. 참조 저장
            team.getMembers().add(member);          //양방향 연관관계 설정. But, 윗줄 없이 이것만 하면 member 의 team_id가 null로 들어간다.

            tx.commit();
        } catch (Exception e) {
            e.printStackTrace();
            tx.rollback();
        } finally {
            em.close();
        }

        System.out.println("hello");
        emf.close();
    }

}
