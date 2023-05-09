package com.example.demo.repository;

import com.example.demo.entity.Employee;
import jakarta.annotation.Nonnull;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.demo.constant.EmployeeSQLConstant.*;

public final class EmployeeRepository {

    private static final String URL = "jdbc:postgresql://localhost:5432/postgres";
    private static final String USER = "postgres";
    private static final String PASSWORD = "0";

    private EmployeeRepository() {
    }

    public static Connection getConnection() {

        Connection connection = null;

        try {
            connection = DriverManager.getConnection(URL, USER, PASSWORD);
            if (connection != null) {
                System.out.println("Connected to the PostgreSQL server successfully.");
            } else {
                System.out.println("Failed to make connection!");
            }
        } catch (SQLException sqlException) {
            System.out.println(sqlException);
        }
        return connection;
    }

    public static int save(Employee employee) {
        int status = 0;
        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(INSERT_USER)) {

            setNewEmployeeIntoDB(employee, ps);

            status = ps.executeUpdate();

        } catch (SQLException ex) {
            ex.printStackTrace();
        }
        return status;
    }

    public static int update(Employee employee) {

        int status = 0;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(UPDATE_USER)) {

            setNewEmployeeIntoDB(employee, ps);
            ps.setInt(4, employee.getId());

            status = ps.executeUpdate();

        } catch (SQLException sqlException) {
            sqlException.printStackTrace();
        }
        return status;
    }

    public static int delete(int id) {

        int status = 0;

        try (@Nonnull Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(DELETE_USER)) {
            ps.setInt(1, id);

            status = ps.executeUpdate();

        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return status;
    }

    public static Employee getEmployeeById(int id) {

        Employee employee = null;

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_BY_ID)) {
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            if (rs.next()) {
                employee = getNewEmployee(rs);
            }
        } catch (SQLException exception) {
            exception.printStackTrace();
        }
        return employee;
    }

    public static List<Employee> getAllEmployees() {

        List<Employee> listEmployees = new ArrayList<>();

        try (Connection connection = getConnection();
             PreparedStatement ps = connection.prepareStatement(SELECT_ALL)) {
            ResultSet rs = ps.executeQuery();

            while (rs.next()) {
                Employee employee = getNewEmployee(rs);

                listEmployees.add(employee);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
        return listEmployees;
    }

    private static Employee getNewEmployee(ResultSet resultSet) throws SQLException {
        Employee employee = new Employee();

        employee.setId(resultSet.getInt(1));
        employee.setName(resultSet.getString(2));
        employee.setEmail(resultSet.getString(3));
        employee.setCountry(resultSet.getString(4));
        return employee;
    }

    private static void setNewEmployeeIntoDB(Employee employee, PreparedStatement ps) throws SQLException {
        ps.setString(1, employee.getName());
        ps.setString(2, employee.getEmail());
        ps.setString(3, employee.getCountry());
    }
}
