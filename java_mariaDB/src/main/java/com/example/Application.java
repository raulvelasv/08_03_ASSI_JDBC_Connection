package com.example;
import java.sql.*;
public class  Application{//Plain connection
    private static Connection connection;
    public static void main(String[] args) throws SQLException {
        try {
            OpenDatabaseConnection();
            deleteDataStudents("%");
            readDataStudents();
            insertDataStudents("Daniel", "Río", "drioarizti@cifpfbmoll.eu", "2001-09-29");
            insertDataStudents("Laura", "Gómez", "lgomez@cifpfbmoll.eu", "1995-12-12");
            insertDataStudents("Alejandro", "Martínez", "amartinez@cifpfbmoll.eu", "1988-06-17");
            insertDataStudents("Sofía", "Hernández", "shernandez@cifpfbmoll.eu", "2002-03-05");
            insertDataStudents("Lucía", "García", "lgarcia@cifpfbmoll.eu", "1999-07-08");
            insertDataStudents("Raúl", "Velásquez", "rvelasquezvega@cifpfbmoll.eu", "1999-06-09");
            readDataStudents();
            updateEmailStudents("Raúl", "raulvolley@cifpfbmoll.eu");
            readDataStudents();
            deleteDataStudents("Raúl");
            readDataStudents();
            readDataDepartments();
            insertDataDepartments("Lenguajes de marcas", 15000.50);
            readDataDepartments();
        } finally {
            if (connection != null) {
                closeDatabaseConnection();
            }
        }
    }


    private static void insertDataStudents(String first_name, String last_name, String email, String birthdate) throws SQLException {
        System.out.println("Inserting data...");
        int rowsInserted;
        try (PreparedStatement st = connection.prepareStatement("""
              INSERT INTO students(first_name, last_name, email, birthdate)
              values(?, ?, ?, ?);
            """)) {
            st.setString(1, first_name);
            st.setString(2, last_name);
            st.setString(3, email);
            st.setString(4, birthdate);
            rowsInserted = st.executeUpdate();
        }
        System.out.println("Rows inserted : " + rowsInserted);
    }


    private static void readDataStudents() throws SQLException {
        System.out.println("Reading data...");
        boolean empty = true;
        try (PreparedStatement st = connection.prepareStatement("""
              SELECT *
              FROM students;
            """)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                int student_id = rs.getInt("student_id");
                String first_name = rs.getString("first_name");
                String last_name = rs.getString("last_name");
                String email = rs.getString("email");
                String birthdate = rs.getString("birthdate");
                int age = rs.getInt("age");
                System.out.println("\t>  "+ student_id + " | " + first_name + " | " + last_name + " | " + email + " | " + birthdate + " | " + age + " ");
                empty = false;
            }
        }
        if (empty) {
            System.out.println("\t (no data)");
        }
    }


    private static void updateEmailStudents(String name, String new_email) throws SQLException {
        System.out.println("Updating data...");
        int rowsUpdated;
        try (PreparedStatement st = connection.prepareStatement("""
              UPDATE students
              SET email = ?
              WHERE first_name = ?;
            """)) {
            st.setString(1, new_email);
            st.setString(2, name);
            rowsUpdated = st.executeUpdate();
        }
        System.out.println("Rows updated : " + rowsUpdated);
    }


    private static void deleteDataStudents(String first_name) throws SQLException {
        System.out.println("Deleting data...");
        int rowsDeleted;
        try (PreparedStatement st = connection.prepareStatement("""
              DELETE FROM students
              WHERE first_name like ?;
            """)) {
            st.setString(1, first_name);
            rowsDeleted = st.executeUpdate();
        }
        System.out.println("Rows deleted: " + rowsDeleted);
    }


    private static void insertDataDepartments(String dept_name, double budget) throws SQLException {
        System.out.println("Inserting data into the departments relation...");
        int rowsInserted;
        try (PreparedStatement st = connection.prepareStatement("""
              INSERT INTO departments(dept_name, budget)
              values(?, ?);
            """)) {
            st.setString(1, dept_name);
            st.setDouble(2, budget);
            rowsInserted = st.executeUpdate();
        }
        System.out.println("Rows inserted : " + rowsInserted);
    }


    private static void readDataDepartments() throws SQLException {
        System.out.println("Reading data from the departments relation...");
        boolean empty = true;
        try (PreparedStatement st = connection.prepareStatement("""
              SELECT *
              FROM departments;
            """)) {
            ResultSet rs = st.executeQuery();
            while (rs.next()) {
                String dept_name = rs.getString("dept_name");
                String classroom = rs.getString("classroom");
                double budget = rs.getInt("budget");
                System.out.println("\t>  "+ dept_name + " | " + classroom + " | " + budget + " ");
                empty = false;
            }
        }
        if (empty) {
            System.out.println("\t (no data)");
        }
    }


    private static void OpenDatabaseConnection() throws SQLException {
        System.out.println("Connecting to the database...");
        connection = DriverManager.getConnection("jdbc:mariadb://localhost:3305/java_mariaDB","root", "1234");
        System.out.println("Connection valid: " + connection.isValid(5));
    }


    private static void closeDatabaseConnection() throws SQLException {
        System.out.println("Closing database connection...");
        connection.close();
        System.out.println("Connection valid: " + connection.isValid(5));
    }
}

