package iot.lab.iot_attendance.Collections;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity(name="tbl_members_attendance")
public class Attendance {
    @Id
    @GeneratedValue
    private int attendanceId;

    public Attendance(LocalDateTime inTime, LocalDateTime outTime, int isActive, String workUpdates) {
        this.inTime = inTime;
        this.outTime = outTime;
        this.isActive = isActive;
        this.workUpdates = workUpdates;

    }

    public Attendance(int isActive, String workUpdates) {

        this.isActive = isActive;
        this.workUpdates = workUpdates;

    }

    @JsonIgnore
    @JoinColumn(name = "member_id")
    @ManyToOne(fetch= FetchType.EAGER)
    private Member member;

    private LocalDateTime inTime;
    private LocalDateTime outTime;
    private int isActive;
    private String workUpdates;
    private LocalDateTime createTime = LocalDateTime.now();

    public Attendance() {

    }
}
