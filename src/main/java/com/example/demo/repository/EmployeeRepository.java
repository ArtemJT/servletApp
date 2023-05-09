package com.example.demo.repository;

import com.example.demo.entity.Employee;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.constant.EmployeeSQLConstant.*;

public final class EmployeeRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0";
    private static Connection connection;

    private EmployeeRepository() {
    }

    public static int save(Employee employee) {
        int status = 0;

        getConnection();

        try (PreparedStatement ps = connection.prepareStatement(INSERT_USER)) {
            saveNewEmployeeIntoDB(employee, ps);

            status = ps.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }

        closeConnection();
        return status;
    }

    public static int update(Employee employee) {

        int status = 0;

        getConnection();

        try (PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {
            saveNewEmployeeIntoDB(employee, ps);
            ps.setInt(4, employee.getId());

            status = ps.executeUpdate();
        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }

        closeConnection();
        return status;
    }

    public static int delete(int id) {

        int status = 0;

        getConnection();

        try (PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {
            ps.setInt(1, id);

            status = ps.executeUpdate();
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        closeConnection();
        return status;
    }

    public static List<Employee> getAllEmployees() {

        List<Employee> listEmployees = new ArrayList<>();

        getConnection();

        try (PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = getNewEmployee(rs);

                listEmployees.add(employee);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        closeConnection();
        return listEmployees;
    }

    public static Employee getEmployeeById(int id) {

        Employee employee = null;

        getConnection();
        try (PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);

            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee = getNewEmployee(rs);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }

        closeConnection();
        return employee;
    }

    private static void getConnection() {

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            System.out.println("Connected to the PostgreSQL server successfully.");
        } catch (SQLException sqlException) {
            System.out.println("Failed to make connection!");
            sqlException.printStackTrace();
        }
    }

    private static void closeConnection() {
        try {
            if (!connection.isClosed()) {
                connection.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static Employee getNewEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();

        employee.setId(resultSet.getInt(1));
        employee.setName(resultSet.getString(2));
        employee.setEmail(resultSet.getString(3));
        employee.setCountry(resultSet.getString(4));
        return employee;
    }

    private static void saveNewEmployeeIntoDB(Employee employee, PreparedStatement ps) throws SQLException {
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getEmail());
        ps.setString(3, employee.getCountry());
    }
}
