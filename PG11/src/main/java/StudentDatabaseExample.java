import java.sql.*;
import java.util.Scanner;

public class StudentDatabaseExample {

    private static final String JDBC_URL = "jdbc:mysql://localhost:3306/studentdb";
    private static final String USERNAME = "root";
    private static final String PASSWORD = "ise123";

    public static void main(String[] args) {
        try {
            // Register JDBC driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Open a connection
            Connection conn = DriverManager.getConnection(JDBC_URL, USERNAME, PASSWORD);

            // Create a Scanner object for user input
            Scanner scanner = new Scanner(System.in);

            // Main menu loop
            while (true) {
                System.out.println("\nStudent Database Operations:");
                System.out.println("1. Insert Data");
                System.out.println("2. Update Data");
                System.out.println("3. Delete Data");
                System.out.println("4. Search Student");
                System.out.println("5. Display All Students");
                System.out.println("6. Exit");
                System.out.print("Enter your choice: ");
                
                int choice = scanner.nextInt();
                scanner.nextLine(); // Consume newline character
                
                switch (choice) {
                    case 1:
                        // Insert Data
                        System.out.print("Enter name: ");
                        String name = scanner.nextLine();
                        System.out.print("Enter age: ");
                        int age = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        System.out.print("Enter major: ");
                        String major = scanner.nextLine();
                        insertData(conn, name, age, major);
                        break;
                    case 2:
                        // Update Data
                        System.out.print("Enter name of student to update: ");
                        String updateName = scanner.nextLine();
                        System.out.print("Enter new age: ");
                        int newAge = scanner.nextInt();
                        scanner.nextLine(); // Consume newline character
                        updateData(conn, updateName, newAge);
                        break;
                    case 3:
                        // Delete Data
                        System.out.print("Enter name of student to delete: ");
                        String deleteName = scanner.nextLine();
                        deleteData(conn, deleteName);
                        break;
                    case 4:
                        // Search Student
                        System.out.print("Enter name of student to search: ");
                        String searchName = scanner.nextLine();
                        searchStudent(conn, searchName);
                        break;
                    case 5:
                        // Display All Students
                        displayAllStudents(conn);
                        break;
                    case 6:
                        // Exit the program
                        System.out.println("Exiting...");
                        conn.close(); // Close database connection
                        scanner.close(); // Close scanner
                        return;
                    default:
                        System.out.println("Invalid choice. Please enter a number from 1 to 6.");
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void insertData(Connection conn, String name, int age, String major) throws SQLException {
        String query = "INSERT INTO Student (name, age, major) VALUES (?, ?, ?)";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        pstmt.setInt(2, age);
        pstmt.setString(3, major);
        pstmt.executeUpdate();
        System.out.println("Data inserted successfully.");
        pstmt.close();
    }

    public static void updateData(Connection conn, String name, int newAge) throws SQLException {
        String query = "UPDATE Student SET age = ? WHERE name = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setInt(1, newAge);
        pstmt.setString(2, name);
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Data updated successfully.");
        } else {
            System.out.println("No student found with name '" + name + "'.");
        }
        pstmt.close();
    }

    public static void deleteData(Connection conn, String name) throws SQLException {
        String query = "DELETE FROM Student WHERE name = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        int rowsAffected = pstmt.executeUpdate();
        if (rowsAffected > 0) {
            System.out.println("Data deleted successfully.");
        } else {
            System.out.println("No student found with name '" + name + "'.");
        }
        pstmt.close();
    }

    public static void searchStudent(Connection conn, String name) throws SQLException {
        String query = "SELECT * FROM Student WHERE name = ?";
        PreparedStatement pstmt = conn.prepareStatement(query);
        pstmt.setString(1, name);
        ResultSet rs = pstmt.executeQuery();

        boolean found = false;
        while (rs.next()) {
            int id = rs.getInt("id");
            String studentName = rs.getString("name");
            int age = rs.getInt("age");
            String major = rs.getString("major");

            System.out.println("ID: " + id + ", Name: " + studentName + ", Age: " + age + ", Major: " + major);
            found = true;
        }

        if (!found) {
            System.out.println("No student found with name '" + name + "'.");
        }

        rs.close();
        pstmt.close();
    }

    public static void displayAllStudents(Connection conn) throws SQLException {
        String query = "SELECT * FROM Student";
        Statement stmt = conn.createStatement();
        ResultSet rs = stmt.executeQuery(query);

        while (rs.next()) {
            int id = rs.getInt("id");
            String studentName = rs.getString("name");
            int age = rs.getInt("age");
            String major = rs.getString("major");

            System.out.println("ID: " + id + ", Name: " + studentName + ", Age: " + age + ", Major: " + major);
        }

        rs.close();
        stmt.close();
    }
}



/*CREATE DATABASE studentdb;
USE studentdb;
CREATE TABLE Student (
id INT AUTO_INCREMENT PRIMARY KEY,
name VARCHAR(50) NOT NULL,
age INT NOT NULL,
major VARCHAR(50) NOT NULL
);
//
use studentdb;
select * from student; */
