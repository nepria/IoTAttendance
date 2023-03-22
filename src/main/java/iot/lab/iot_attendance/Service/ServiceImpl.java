package iot.lab.iot_attendance.Service;

import iot.lab.iot_attendance.Collections.Attendance;
import iot.lab.iot_attendance.Collections.Domain;
import iot.lab.iot_attendance.Collections.Member;
import iot.lab.iot_attendance.Repository.AttendanceRepository;
import iot.lab.iot_attendance.Repository.DomainRepository;
import iot.lab.iot_attendance.Repository.MemberRepository;
import iot.lab.iot_attendance.Repository.RoleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class ServiceImpl implements AttendanceService{
    @Autowired
    private MemberRepository memberRepository;

    @Autowired
    private DomainRepository domainRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private AttendanceRepository attendanceRepository;


    @Override
    public String save(Member member) {
        return memberRepository.save(member).getEmail();
    }

    @Override
    public int save(Domain domain) {
        return domainRepository.save(domain).getId();
    }

    @Override
    public int save(Attendance attendance) {
        return attendanceRepository.save(attendance).getAttendanceId();
    }

    @Override
    public int getDomainId(String domainName) {
        return domainRepository.findDomainByDomainName(domainName).getId();
    }

    @Override
    public void deleteAll() {
        domainRepository.deleteAll();
    }

    @Override
    public int getRoleId(String roleName) {
        return roleRepository.findRoleByName(roleName).getRoleId();
    }

    @Override
    public int getMemberIdByRoll(String roll) {
        return memberRepository.findMemberByRoll(roll).getMemberId();
    }

    public Member getMemberByRoll(String roll) {
        return memberRepository.findMemberByRoll(roll);
    }

    @Override
    public Attendance getEntryByIdDate(String roll, LocalDateTime now) {
        Member member = getMemberByRoll(roll);
        java.sql.Date sqlDate = java.sql.Date.valueOf(now.toLocalDate());
        Attendance entry = attendanceRepository.findFirstByMemberWithInTimeDate(member, sqlDate);

        return entry;
    }

    @Override
    public List<Attendance> getAllRecords(LocalDate start, LocalDate end) {
        java.sql.Date sqlDateStart = java.sql.Date.valueOf(start);
        java.sql.Date sqlDateEnd = java.sql.Date.valueOf(end);
        List<Attendance> attendances = attendanceRepository.findAllAttendanceWithinDate(sqlDateStart, sqlDateEnd);
        return attendances;
    }

    @Override
    public List<Attendance> getMemberAttendance(String roll, LocalDate start, LocalDate end) {
        Member member = getMemberByRoll(roll);
        java.sql.Date sqlDateStart = java.sql.Date.valueOf(start);
        java.sql.Date sqlDateEnd = java.sql.Date.valueOf(end);
        List<Attendance> attendances = attendanceRepository.findMemberAttendanceWithinDate(member, sqlDateStart, sqlDateEnd);
        return attendances;
    }

    @Override
    public Attendance getEntryByIdDateUpdates(String roll, LocalDateTime now) {
        Member member = getMemberByRoll(roll);
        java.sql.Date sqlDate = java.sql.Date.valueOf(now.toLocalDate());
        Attendance entry = attendanceRepository.findMemberAttendanceWhereInOutUpdateNull(member, sqlDate);
        return entry;
    }

}
