package vn.edu.likelion.TeacherAuthen.service;

import vn.edu.likelion.TeacherAuthen.data.Connect;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * WriteFileToDatbase -
 *
 * @param
 * @return
 * @throws
 */
public class WriteFileToDatbase {


    public static void insertStudents(Connect conn,  PreparedStatement stat,ResultSet resultSet, String filePath) {

        try {
            String sql = "INSERT INTO student (id, name, attendance) VALUES (?, ?, ?)";
            conn = new Connect();
            stat = conn.openConnect().prepareStatement(sql);

            int batchSize = 1000;
            int count = 0;

            try (BufferedReader br = new BufferedReader(new FileReader(filePath))) {
                String line;
                while ((line = br.readLine()) != null) {
                    String[] parts = line.split("\t");
                    if (parts.length == 3) {
                        try {
                            int id = Integer.parseInt(parts[0].trim());
                            String name = parts[1].trim();
                            int attendance = Integer.parseInt(parts[2].trim());

                            stat.setInt(1, id);
                            stat.setString(2, name);
                            stat.setInt(3, attendance);
                            stat.addBatch();

                            if (++count % batchSize == 0) {
                                stat.executeBatch();
                            }
                        } catch (NumberFormatException e) {
                            System.err.println("Error parsing number in line: " + line);
                        }
                    } else {
                        System.err.println("Invalid line format: " + line);
                    }
                }
                if (count % batchSize != 0) {
                    stat.executeBatch();
                }
            }
        } catch (SQLException | IOException e) {
            e.printStackTrace();
        } finally {
            if (stat != null) {
                try {
                    stat.close();
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }
}
