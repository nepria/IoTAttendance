package iot.lab.iot_attendance.Repository;

import iot.lab.iot_attendance.Collections.Attendance;
import iot.lab.iot_attendance.Collections.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


@EnableJpaRepositories
@Repository
public interface AttendanceRepository extends JpaRepository<Attendance, Integer> {


      @Query("select att from tbl_members_attendance att where  att.member = :member and DATE(att.inTime) = :date and att.isActive =1")
      Attendance findFirstByMemberWithInTimeDate(Member member, java.sql.Date date);

      @Query("select att from tbl_members_attendance att where DATE(att.createTime) between :start and :end")
      List<Attendance> findAllAttendanceWithinDate(java.sql.Date start, java.sql.Date end);

      @Query("select att from tbl_members_attendance att where att.member = :member  and DATE(att.createTime) between :start and :end")
      List<Attendance> findMemberAttendanceWithinDate(Member member, java.sql.Date start, java.sql.Date end);

      //Get Attendance entry for a member on the current date where in is done out is done but update is missing
      @Query("select att from tbl_members_attendance att where  att.member = :member and DATE(att.createTime) = :date and att.isActive =2 and att.workUpdates is null")
      Attendance findMemberAttendanceWhereInOutUpdateNull(Member member, java.sql.Date date);

}
