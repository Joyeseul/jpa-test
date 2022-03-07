package hellojpa;

import hellojpa.entity.Member;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main03_Column_Mapping {

    public static void main(String[] args) {

        /* 엔티티 매니저 팩토리 */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");
        EntityManager em = emf.createEntityManager();       //엔티티 매니저
        EntityTransaction tx = em.getTransaction();         //트랜잭션 받기
        tx.begin();                                         //트랜젝션 시작

        try{
            /* 회원 등록
            * 식별자 : Long 타입 & 대체키 & 키 생성 전략을 사용할 것
            *  */
            Member member = new Member();
//            member.setId(1L);             //엔터티에서 @GeneratedValue의 전략을 AUTO로 해두면, 자동 시퀀스를 따게 할 수 있다.
            member.setName("hello");
//            member.setMemberType(MemberType.ADMIN);
//            member.setTeamId(team.getId());       //JPA를 안 쓸 경우.

            em.persist(member);

        } catch (Exception e) {
            tx.rollback();
        } finally {
            em.close();
        }

        System.out.println("hello");
        emf.close();                                    //웹프로젝트를 아예 내릴 때
    }
}
