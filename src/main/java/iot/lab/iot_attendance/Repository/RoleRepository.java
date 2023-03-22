package iot.lab.iot_attendance.Repository;


import iot.lab.iot_attendance.Collections.Role;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RoleRepository extends JpaRepository<Role, Integer> {
    Role findRoleByName(String roleName);

}