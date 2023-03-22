package iot.lab.iot_attendance.Repository;

import iot.lab.iot_attendance.Collections.Member;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface MemberRepository extends JpaRepository<Member, Integer> {
      Member findMemberByRoll(String rollNumber);
}
//tujhe mai pyaar kru