
package iot.lab.iot_attendance.Service;
import iot.lab.iot_attendance.Collections.Attendance;
import iot.lab.iot_attendance.Collections.Domain;
import iot.lab.iot_attendance.Collections.Member;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;


public interface AttendanceService {
    String save(Member member);

    int save(Domain domain);

    int save(Attendance attendance);

    int getDomainId(String domainName);

    void deleteAll();

    int getRoleId(String RoleName);

    int getMemberIdByRoll(String roll);

    Attendance getEntryByIdDate(String roll, LocalDateTime now);

    List<Attendance> getAllRecords(LocalDate start, LocalDate end);

    List<Attendance> getMemberAttendance(String roll, LocalDate start, LocalDate end);

    Attendance getEntryByIdDateUpdates(String roll, LocalDateTime now);
}
