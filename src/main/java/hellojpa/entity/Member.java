package hellojpa.entity;

import javax.persistence.*;
import java.util.Date;

@Entity
public class Member {

    @Id @GeneratedValue(strategy = GenerationType.AUTO)
    private Long id;

    @Column(name = "USERNAME", nullable = false, length = 20)
    private String name;
    private int age;

    //@Enumerated(EnumType.ORDINAL)     //ORDINAL 이면 순번이 들어가고, 나중에 그 순번의 변경이 생기면 에러남
    @Enumerated(EnumType.STRING)        //따라서 무조건 STRING 으로 넣어야 함
    private MemberType memberType;

    @Temporal(TemporalType.TIMESTAMP)
    private Date regDate;

    public Long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public MemberType getMemberType() {
        return memberType;
    }

    public void setMemberType(MemberType memberType) {
        this.memberType = memberType;
    }

    public Date getRegDate() {
        return regDate;
    }

    public void setRegDate(Date regDate) {
        this.regDate = regDate;
    }
}
