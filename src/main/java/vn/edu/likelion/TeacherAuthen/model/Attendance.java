package vn.edu.likelion.TeacherAuthen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

/**
 * Attendance -
 *
 * @param
 * @return
 * @throws
 */

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Attendance {
    private long attendanceId;
    private  Student studentId;
    private int status;
    private LocalDateTime attendanceDay;


}
