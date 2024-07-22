package vn.edu.likelion.TeacherAuthen.model;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.util.ArrayList;
import java.util.List;

/**
 * Student -
 *
 * @param
 * @return
 * @throws
 */
@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Student {

    private long id;
    private String name;
    private Attendance attendance;
    private List<Attendance> attendances = new ArrayList<>();
    public void addAttendance(Attendance attendance) {
        this.attendances.add(attendance);
    }

}
