package iot.lab.iot_attendance.Repository;


import iot.lab.iot_attendance.Collections.Domain;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DomainRepository extends JpaRepository<Domain, Integer> {
    Domain findDomainByDomainName(String name);

}