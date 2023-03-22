package iot.lab.iot_attendance.Collections;

import jakarta.persistence.*;
import lombok.Data;
import java.util.List;

@Data
@Entity(name="tbl_domain")
public class Domain {
    @Id
    @GeneratedValue
    private int id;

    private String domainName;
    private int domainStrength;


    @OneToMany(cascade = CascadeType.ALL, mappedBy = "domain", fetch= FetchType.EAGER)
    private List<Member> members;

    public Domain(int id, String domain_name, int domain_strength) {
        this.id = id;
        this.domainName = domain_name;
        this.domainStrength = domain_strength;
    }

    public Domain(String domain_name) {
        this.domainName = domain_name;
    }

    public Domain(int id) {
        this.id = id;
    }

    public Domain() {

    }

}
