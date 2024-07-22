package vn.edu.likelion.TeacherAuthen.service;

import vn.edu.likelion.TeacherAuthen.dao.UserDAO;
import vn.edu.likelion.TeacherAuthen.model.Attendance;
import vn.edu.likelion.TeacherAuthen.model.Student;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

/**
 * UserService -
 *
 * @param
 * @return
 * @throws
 */
public class UserService {
    static Scanner scanner = new Scanner(System.in);


    public List<String> generateDates(LocalDate startDate, int numberOfDays) {
        List<String> dates = new ArrayList<>();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        for (int i = 0; i < numberOfDays; i++) {
            dates.add(startDate.plusDays(i).format(formatter));
        }
        return dates;
    }

    /**
     * method use for get All student with Date
     * @param students
     * @param listDates
     *
     */
    public static void displayAllStudents(List<Student> students, List<Attendance> listDates) {
        System.out.printf("%-5s | %-30s |", "ID", "Họ và Tên");
        for (Attendance date : listDates) {
            String formattedDate = date.getAttendanceDay().toString().split("T")[0];
            System.out.printf(" %-10s |", formattedDate);
        }
        System.out.println();

        System.out.println("-".repeat(50 + listDates.size() * 13));

        for(Student student : students) {
            System.out.printf("%-5d | %-30s |", student.getId(), student.getName());

            for(Attendance date : listDates) {
                System.out.printf(" %-10d |", date.getStatus());
            }
            System.out.println();
        }
    }

    /**
     * Method to display Student Present and not Present
     * @param students
     * @param date
     */
    public static void displayStudentsPresent(List<Student> students, String date) {
        System.out.printf("%-5s | %-30s | %-10s | %s%n", "ID", "Họ và Tên", date, "Trạng thái");
        System.out.println("-".repeat(60));
        for (Student student : students) {
            System.out.printf("%-5d | %-30s | %-10d | %s%n",
                    student.getId(),
                    student.getName(),
                    student.getAttendance().getStatus(),
                    (student.getAttendance().getStatus() == 1 ? "Có mặt" : "Vắng mặt"));
        }
    }

    /**
     * Method use to check attendance function
     * @param userDAO
     */
    public static void checkAttendance(UserDAO userDAO) {
        int id = 1 ;

        System.out.println("Bắt đầu điểm danh !!!!!!!!");
        System.out.print("Xin vui lòng nhập ngày bạn muốn điểm danh (yyy-MM-dd): ");
        String day = scanner.nextLine();
        LocalTime localTime = LocalTime.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("HH:mm:ss");
        String time  = localTime.format(formatter);
        String attendanceDay = day+" "+time;


        while (id <= 23) {
            id++;
            String nameStudent = userDAO.getNameStudent(id);
            System.out.println("Bạn có muốn điểm danh: "+nameStudent);
            System.out.println("Nhấn Y/y để (có) và N/n để (không)");
            String choose = scanner.nextLine();
            if(choose.equalsIgnoreCase("Y")) {
                userDAO.updateMarkAttendance(id,1,attendanceDay);
            }else if (choose.equalsIgnoreCase("N")) {
                userDAO.updateMarkAttendance(id,0,attendanceDay);
            }
        }
        System.out.println("Điểm danh kết thúc!!!!");

    }


    /**
     * Method use for check account for login
     * @param userDAO
     * @return true if it login successful
     */
    public static boolean login(UserDAO userDAO) {

        System.out.print("Enter your email to login: ");
        String email = scanner.nextLine();
        System.out.print("Enter your password to login: ");
        String loginPassword = scanner.nextLine();

        String storedHashedPassword = userDAO.findEmail(email);
        if (storedHashedPassword == null) {
            System.out.println("Email not found");
        }else {
            boolean isPasswordCorrect = verifyPassword(loginPassword,storedHashedPassword );
            if (isPasswordCorrect) {
                System.out.println("Login successfully.");
            }else {
                System.out.println("Password not correct.");
            }
        }
        return true;
    }

    /**
     *  hashing password to SHA-256
     * @param password
     * @return String's code is hashed
     */
    public static String hashPassword(String password) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            byte[] hash = md.digest(password.getBytes());
            StringBuilder hexString = new StringBuilder();
            for (byte b : hash) {
                hexString.append(String.format("%02x", b));
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            throw new RuntimeException(e);
        }
    }


    /**
     * method use for verify password to compare password and hash is store
     * @param originalPassword
     * @param storedHash
     * @return true if verify successfull
     */
    public static boolean verifyPassword(String originalPassword, String storedHash) {
        String hashedPassword = hashPassword(originalPassword);
        return hashedPassword.equals(storedHash);
    }
}
