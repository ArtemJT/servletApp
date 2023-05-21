package com.example.demo.service;

import com.example.demo.entity.Employee;
import com.example.demo.exception.EmployeeNotFoundException;
import com.example.demo.repository.EmployeeRepository;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.ws.rs.BadRequestException;

import java.util.List;

/**
 * @author Artem Kovalov on 09.05.2023
 */
public final class EmployeeService {

    private EmployeeService() {
    }

    public static void saveEmployee(HttpServletRequest request)
            throws EmployeeNotFoundException, BadRequestException {
        Employee employee = getEmployee(request);

        int status = EmployeeRepository.save(employee);

        if (status == 0) {
            throw new EmployeeNotFoundException("Saving employee error");
        }
    }

    public static void updateEmployee(HttpServletRequest request)
            throws EmployeeNotFoundException, BadRequestException {

        Employee employee = getEmployeeWithId(request);

        int status = EmployeeRepository.update(employee);

        if (status == 0) {
            throw new EmployeeNotFoundException(employee.id());
        }
    }

    public static void deleteById(HttpServletRequest request)
            throws EmployeeNotFoundException, BadRequestException {
        int id = getIdValueFromParameter(request);

        int status = EmployeeRepository.delete(id);

        if (status == 0) {
            throw new EmployeeNotFoundException(id);
        }
    }

    public static Employee getById(HttpServletRequest request)
            throws EmployeeNotFoundException, BadRequestException {

        int id = getIdValueFromParameter(request);

        Employee employeeById = EmployeeRepository.getEmployeeById(id);

        if (employeeById == null) {
            throw new EmployeeNotFoundException(id);
        }

        return employeeById;
    }

    public static List<Employee> getAll() throws EmployeeNotFoundException {

        List<Employee> allEmployees = EmployeeRepository.getAllEmployees();

        if (allEmployees.isEmpty()) {
            throw new EmployeeNotFoundException("No employees in database");
        }
        return allEmployees;
    }

    private static int getIdValueFromParameter(HttpServletRequest request) throws BadRequestException {
        String parameterName = "id";
        String parameterValue = request.getParameter(parameterName);

        if (parameterValue == null) {
            throw new BadRequestException();
        }

        try {
            return Integer.parseInt(parameterValue);
        } catch (NumberFormatException e) {
            throw new BadRequestException();
        }
    }

    private static Employee getEmployeeWithId(HttpServletRequest request) throws BadRequestException {

        int id = getIdValueFromParameter(request);
        return getEmployee(request, id);
    }

    private static Employee getEmployee(HttpServletRequest request, int... requestId) throws BadRequestException {

        String name = request.getParameter("name");
        String email = request.getParameter("email");
        String country = request.getParameter("country");

        if (!checkStringParameters(name, email, country)) {
            throw new BadRequestException();
        }

        int id = requestId.length == 0 ? requestId.length : requestId[0];

        return new Employee(id, name, country, email);
    }

    private static boolean checkStringParameters(String... parameters) {
        for (String s : parameters) {
            if (s == null || s.equals("")) {
                return false;
            }
        }
        return true;
    }
}
