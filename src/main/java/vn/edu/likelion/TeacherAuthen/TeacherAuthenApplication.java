package vn.edu.likelion.TeacherAuthen;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import vn.edu.likelion.TeacherAuthen.dao.UserDAO;
import vn.edu.likelion.TeacherAuthen.data.Connect;
import vn.edu.likelion.TeacherAuthen.model.Attendance;
import vn.edu.likelion.TeacherAuthen.model.Student;
import vn.edu.likelion.TeacherAuthen.model.Users;
import vn.edu.likelion.TeacherAuthen.service.WriteFileToDatbase;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

import static vn.edu.likelion.TeacherAuthen.service.UserService.*;


public class TeacherAuthenApplication {


	private static final Scanner scanner = new Scanner(System.in);
	private static final UserDAO userDAO = new UserDAO();
	static List<Student> students;
	static List<Attendance> listDates;


	public static void main(String[] args) throws IOException {

		String filePath = "E:/LIKELION/Practice/TeacherAuthen/StudentsList.txt";

		// WriteFileToDatbase.insertStudents(conn,stat,resultSet,filePath);




		if (login(userDAO)) {
			displayMenuForAdmin();
		}else {
			System.out.println("Đăng nhập thất bại");
		}
	}
	private static void displayMenuForAdmin() {
		int choose;
		do {
			System.out.println("Menu:");
			System.out.println("1. Xem danh sách học viên");
			System.out.println("2. Xem danh sách học viên vắng mặt");
			System.out.println("3. Xem danh sách học viên có mặt");
			System.out.println("4. Kiểm tra điểm danh hôm nay");
			System.out.println("5. Điểm danh học viên");
			System.out.println("6. Thoát");
			System.out.print("Nhập lựa chọn của bạn: ");
			choose = scanner.nextInt();
			scanner.nextLine(); // consume newline
			switch (choose) {
				case 1:
					listDates = UserDAO.getListDate();
					students = UserDAO.getAllStudent();
					displayAllStudents(students,listDates);
					break;
				case 2:

					System.out.print("Chọn ngày để xem điểm danh (yyyy-MM-dd): ");
					String date1 = scanner.nextLine();
					students = userDAO.getStudentPresent(date1,0);
					displayStudentsPresent(students,date1);
					break;
				case 3:
					System.out.print("Chọn ngày để xem điểm danh (yyyy-MM-dd): ");
					String date2 = scanner.nextLine();
					students = userDAO.getStudentPresent(date2,1);
					displayStudentsPresent(students,date2);
					break;
				case 4:
					System.out.print("Chọn ngày để xem điểm danh (yyyy-MM-dd): ");
					String date3 = scanner.nextLine();
					students = userDAO.getStudentAttendance(date3);
					displayStudentsPresent(students,date3);

					break;
				case 5:
					checkAttendance(userDAO);
					break;
				case 6:
					System.out.println("Thoát chương trình...");
					break;
				default:
					System.out.println("Lựa chọn không hợp lệ. Vui lòng thử lại.");
			}
		} while (choose != 6);
	}




}
