package com.example.demo.exception;

/**
 * @author Artem Kovalov on 09.05.2023
 */
public class EmployeeNotFoundException extends Exception {

    private final String message;

    public EmployeeNotFoundException(int id) {
        this.message = "Employee with id=" + id + " not found";
    }

    public EmployeeNotFoundException(String message) {
        this.message = message;
    }

    @Override
    public String getMessage() {
        return this.message;
    }
}
