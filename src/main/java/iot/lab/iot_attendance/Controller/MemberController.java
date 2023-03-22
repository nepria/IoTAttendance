package iot.lab.iot_attendance.Controller;

import iot.lab.iot_attendance.Collections.Attendance;
import iot.lab.iot_attendance.Collections.Domain;
import iot.lab.iot_attendance.Collections.Member;
import iot.lab.iot_attendance.Collections.Role;
import iot.lab.iot_attendance.Service.AttendanceService;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

@RestController
@RequestMapping("attendance")
public class MemberController {

    @Autowired
    private AttendanceService attendanceService;


    //Mark in entry
    @PostMapping("in")
    public ResponseEntity markIn(@RequestParam("roll") String roll) {
        int memberId = attendanceService.getMemberIdByRoll(roll);
        //Check whether there is an attendance entry with "in but not out" in that case "first out then in".
        Attendance entryToday = attendanceService.getEntryByIdDate(roll, LocalDateTime.now());

        //Get attendance entry where update is missing after out
        Attendance entryWithUpdatesLeft = attendanceService.getEntryByIdDateUpdates(roll, LocalDateTime.now());

        //isActive parameters:- 0 by default, 1 with in and 2 when out done

        if(entryToday == null && entryWithUpdatesLeft == null) { //No pending attendance entry with in out and updates done

            Attendance attendance = new Attendance(LocalDateTime.now(), null, 1, null);
            attendance.setMember(new Member(memberId));
            attendanceService.save(attendance);
            return  ResponseEntity.ok().body("Attendance marked");
        }
        //Updates of previous entry not added
        else if(entryWithUpdatesLeft != null)
            return ResponseEntity.badRequest().body("First Add Updates");

        //No out for previous entry
        return ResponseEntity.badRequest().body("First out then in ");


    }

    @PostMapping("out")
    public ResponseEntity<String> markOut(@RequestParam("roll") String roll) {

        Attendance isEntryToday = attendanceService.getEntryByIdDate(roll, LocalDateTime.now());

        //Get The Latest Entry of in
        if(isEntryToday != null) {

            isEntryToday.setOutTime(LocalDateTime.now());
            isEntryToday.setIsActive(2);
            attendanceService.save(isEntryToday);

            return ResponseEntity.ok().body("Out marked");
        }
        //No entry to mark out time for
        return ResponseEntity.badRequest().body("First in then out");

    }

    @PostMapping("updates")
    public ResponseEntity<String> markUpdates(@RequestParam("roll") String roll, @RequestParam("updates") String updates) {
        Attendance entryWithUpdatesLeft = attendanceService.getEntryByIdDateUpdates(roll, LocalDateTime.now());

        //There is an entry with no updates given then add updates to it
        if(entryWithUpdatesLeft != null) {
            entryWithUpdatesLeft.setWorkUpdates(updates);
            attendanceService.save(entryWithUpdatesLeft);
            return ResponseEntity.ok().body("Updates have been added");
        }
        return ResponseEntity.badRequest().body("No entry there to put updates for");
    }

    //Get All attendance with start and end date
    @GetMapping("get/all")
    public List<Attendance> getAllAttendance(@RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<Attendance> attendances = attendanceService.getAllRecords(start, end);
        return attendances;

    }

    //Get Attendance of a roll number with start and end date
    @GetMapping("get/memberAttendance")
    public List<Attendance> getMemberAttendance(@RequestParam("roll") String roll, @RequestParam("startDate") String startDate, @RequestParam("endDate") String endDate) {

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        LocalDate start = LocalDate.parse(startDate, formatter);
        LocalDate end = LocalDate.parse(endDate, formatter);
        List<Attendance> attendances = attendanceService.getMemberAttendance(roll, start, end);
        return attendances;

    }

    //Create 1 member
    @PostMapping("member/create")
    public String save(@RequestBody Member member, @RequestParam("domainName") String domainName) {
        int domainId = attendanceService.getDomainId(domainName);
        member.setDomain(new Domain(domainId));
        return attendanceService.save(member);
    }

    //Import all members from xl file
    @GetMapping("import/members")
    public String importMembers(@RequestParam("file") MultipartFile file) throws IOException {
        FileInputStream fis = new FileInputStream(convert(file));
        DataFormatter dataFormatter = new DataFormatter();

        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);

        for(Row row: sheet) {
            String domainName = String.valueOf(row.getCell(5));
            System.out.println(domainName);
            int domainId = attendanceService.getDomainId(domainName.trim());
            List<String> memberDetails = new ArrayList<>();
            int count = 0;
            for(Cell cell: row) {
                if(count++ == 5) break;
                    memberDetails.add(dataFormatter.formatCellValue(cell));
            }
            String roleName = memberDetails.get(3);
            int roleId = attendanceService.getRoleId(roleName.trim());
            Member member = new Member(memberDetails.get(0), memberDetails.get(1), memberDetails.get(2), memberDetails.get(4));
            member.setDomain(new Domain(domainId));
            member.setRole(new Role(roleId));
            attendanceService.save(member);
        }
        return "Member Uploaded Successfully";
    }

    //Import all domains from xl
    @GetMapping("import/domains")
    public String importDomains(@RequestParam("file") MultipartFile file)  throws IOException {
        FileInputStream fis = new FileInputStream(convert(file));
        XSSFWorkbook wb = new XSSFWorkbook(fis);
        XSSFSheet sheet = wb.getSheetAt(0);

        attendanceService.deleteAll();

        for(Row row: sheet) {
            for(Cell cell: row) {
                Domain object = new Domain(cell.getStringCellValue());
                attendanceService.save(object);
            }
        }
        return "Domain Added Successfully";
    }

    //Convert Multipart File to File type
    public File convert(MultipartFile file) throws IOException {
        File convFile = new File(Objects.requireNonNull(file.getOriginalFilename()));
        FileOutputStream fos = new FileOutputStream(convFile);
        fos.write(file.getBytes());
        fos.close();
        return convFile;
    }


}
