package com.example.demo.constant;

/**
 * @author Artem Kovalov on 09.05.2023
 */
public final class EmployeeSQLConstant {

    public static final String INSERT_USER = "insert into users(name,email,country) values (?,?,?)";
    public static final String UPDATE_USER = "update users set name=?,email=?,country=? where id=?";
    public static final String DELETE_USER = "delete from users where id=?";
    public static final String SELECT_BY_ID = "select * from users where id=?";
    public static final String SELECT_ALL = "select * from users";

    private EmployeeSQLConstant() {
    }
}
