package vn.edu.likelion.TeacherAuthen.dao;

import vn.edu.likelion.TeacherAuthen.data.Connect;
import vn.edu.likelion.TeacherAuthen.model.Attendance;
import vn.edu.likelion.TeacherAuthen.model.Student;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.*;

/**
 * UserDAO - [
 *
 * @param
 * @return
 * @throws
 */
public class UserDAO {
    Connect conn = null;
    PreparedStatement stat = null;
    ResultSet resultSet = null;



    /**
     * method to find email and return hashed password
     * @param email
     * @return  hashed password
     */
    public String findEmail(String email) {
        String hashedPassword = null;
        try {
            String query = "SELECT password from users where email = ? ";
            conn = new Connect();
            stat = conn.openConnect().prepareStatement(query);

            stat.setString(1,email);
            resultSet = stat.executeQuery();
            if (resultSet.next()) {
                hashedPassword = resultSet.getString("password");
            }
        }catch (SQLException e) {
            e.printStackTrace();
        }
        return hashedPassword;
    }



    /**
     * Method use to get NamStudent to mark attendance
     * @param id
     * @return {@code Name student}
     */
    public String getNameStudent(long id) {
        String nameStudent = "";
        try {
            String query = "SELECT name FROM student WHERE id = ? ";
            conn = new Connect();
            stat = conn.openConnect().prepareStatement(query);
            stat.setLong(1,id);
            resultSet = stat.executeQuery();
            if (resultSet.next()) {
                nameStudent = resultSet.getString("name");
            }

        }catch (SQLException sql) {
            sql.printStackTrace();
        }finally {
            try {
                conn.closeConnect();
                if (stat != null) stat.close();
                if (resultSet != null) resultSet.close();
            } catch (SQLException s) {
                s.printStackTrace();
            }
        }

        return nameStudent;
    }

    /**
     * Method use for update Attendance
     * @param studentId
     * @param status
     * @param
     * @return {@code Attendance record inserted successfully}
     */
    public void updateMarkAttendance(long studentId, int status,String attendanceDay) {
        String query = "INSERT INTO attendance (student_id,status,attendance_day) VALUES (?, ?, TO_DATE(?, 'YYYY-MM-DD HH24:MI:SS'))";
        try(Connection conn = new Connect().openConnect();
            PreparedStatement stat = conn.prepareStatement(query)) {
            stat.setLong(1,studentId);
            stat.setInt(2,status);
            stat.setString(3, attendanceDay);

            int rowInserted = stat.executeUpdate();
            if (rowInserted > 0) {
                System.out.println("Attendance record inserted successfully.");
            } else {
                System.out.println("Attendance record already exists for today.");
            }

        }catch (SQLException sql) {
            sql.printStackTrace();
        }finally {
            try {
                if (stat != null) stat.close();
                if (conn != null) conn.closeConnect();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
    }


    /**
     * Use for ge
     * @param day
     * @return
     */
    public List<Student> getStudentPresent(String day, int status) {
        List<Student> presentStudents = new ArrayList<>();
        String query =
                        "SELECT s.id, s.name, COALESCE(a.status, 0) as status FROM student s JOIN attendance a ON s.id = a.student_id " +
                        "WHERE a.status = ? AND TRUNC(a.attendance_day) = TO_DATE(?, 'YYYY-MM-DD') " +
                        "ORDER BY s.id asc";

        try (Connection conn = new Connect().openConnect();
             PreparedStatement stat = conn.prepareStatement(query)) {

            stat.setInt(1,status);
            stat.setString(2, day);


            try (ResultSet resultSet = stat.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getLong("id"));
                    student.setName(resultSet.getString("name"));
                    Attendance attendance = new Attendance();
                    attendance.setStatus(resultSet.getInt("status"));
                    student.setAttendance(attendance);
                    presentStudents.add(student);
                }
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return presentStudents;
    }

    /**
     * Method use for get Student with present and not present
     * @param day
     * @return
     */
    public List<Student> getStudentAttendance(String day) {

        List<Student> presentStudents = new ArrayList<>();

        String query =
                        "SELECT s.id, s.name, COALESCE(a.status, 0) as status " +
                        "FROM student s " +
                        "LEFT JOIN attendance a ON s.id = a.student_id AND TRUNC(a.attendance_day) = TO_DATE(?, 'YYYY-MM-DD') " +
                        "ORDER BY s.id asc";

        try (Connection conn = new Connect().openConnect();
             PreparedStatement stat = conn.prepareStatement(query)) {
            stat.setString(1, day);


            try (ResultSet resultSet = stat.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getLong("id"));
                    student.setName(resultSet.getString("name"));
                    Attendance attendance = new Attendance();
                    attendance.setStatus(resultSet.getInt("status"));
                    student.setAttendance(attendance);
                    presentStudents.add(student);
                }
            }
        } catch (SQLException sql) {
            sql.printStackTrace();
        }
        return presentStudents;
    }

    public static List<Attendance> getListDate() {
        List<Attendance> attendanceList = new ArrayList<>();
        String query = "SELECT distinct attendance_day , status FROM attendance";

        try(Connection conn = new Connect().openConnect();
            PreparedStatement stat = conn.prepareStatement(query)) {
            try (ResultSet resultSet = stat.executeQuery()) {
                while (resultSet.next()) {
                    Attendance attendance = new Attendance();
                    LocalDateTime attendanceDay = resultSet.getObject("attendance_day", LocalDateTime.class);
                    attendance.setAttendanceDay(attendanceDay);
                    attendance.setStatus(resultSet.getInt("status"));
                    attendanceList.add(attendance);
                }
            }
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        return attendanceList;
    }

    public static List<Student> getAllStudent() {

        List<Student> studentList = new ArrayList<>();
        String query = "SELECT id,name FROM student";

        try(Connection conn = new Connect().openConnect();
            PreparedStatement stat = conn.prepareStatement(query)) {
            try (ResultSet resultSet = stat.executeQuery()) {
                while (resultSet.next()) {
                    Student student = new Student();
                    student.setId(resultSet.getLong("id"));
                    student.setName(resultSet.getString("name"));
                    studentList.add(student);
                }
            }
        }catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return studentList;
    }





}
