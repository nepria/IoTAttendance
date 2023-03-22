package iot.lab.iot_attendance.Collections;


import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.List;

@Data
@Entity(name="tbl_roles")
public class Role {
    @Id
    public int roleId;

    public Role(int roleId) {
        this.roleId = roleId;
    }

    public String name;
    private LocalDateTime createTime= LocalDateTime.now();
    @OneToMany(cascade = CascadeType.ALL, mappedBy = "role", fetch= FetchType.EAGER)
    private List<Member> members;

    public Role() {

    }
}
