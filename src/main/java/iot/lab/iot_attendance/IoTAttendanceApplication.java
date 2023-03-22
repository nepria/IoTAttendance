package iot.lab.iot_attendance;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@SpringBootApplication
@EnableJpaRepositories
public class IoTAttendanceApplication {

    public static void main(String[] args) {
        SpringApplication.run(IoTAttendanceApplication.class, args);
    }

}
