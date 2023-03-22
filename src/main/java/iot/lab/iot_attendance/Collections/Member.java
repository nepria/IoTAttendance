package iot.lab.iot_attendance.Collections;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name="tbl_members")
public class Member {


    @Id
    @GeneratedValue
    private int memberId;
    private String name="";
    private String roll="";
    private String email="";
    private String phone="";
    private String password="";
    private LocalDateTime createTime= LocalDateTime.now();

    @JoinColumn(name = "domain_id")
    @ManyToOne(fetch= FetchType.EAGER)
    private Domain domain;

    @JoinColumn(name = "role_id")
    @ManyToOne(fetch= FetchType.EAGER)
    private Role role;



    public Member() { }

    public Member(int memberId) {
        this.memberId = memberId;
    }

    public Member(String name, String email, String phone, String roll) {
        this.name = name;
        this.email = email;
        this.phone = phone;
        this.roll = roll;
          }


}