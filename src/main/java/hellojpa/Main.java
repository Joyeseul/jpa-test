package hellojpa;

import hellojpa.entity.Member;
import hellojpa.entity.MemberType;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.EntityTransaction;
import javax.persistence.Persistence;

public class Main {

    public static void main(String[] args) {
        /* persistance */
        EntityManagerFactory emf = Persistence.createEntityManagerFactory("hello");

        EntityManager em = emf.createEntityManager();       //엔티티 매니저

        EntityTransaction tx = em.getTransaction();
        tx.begin();                                         //트랜젝션 시작

        try{
            Member member = new Member();
            //member.setId(1L);             //엔터티에서 @GeneratedValue의 전략을 AUTO로 해두면, 자동 시퀀스를 따게 할 수 있다.
            member.setName("hello");
            member.setMemberType(MemberType.USER);

            em.persist(member);

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
