package com.example.demo.service;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.BadRequestException;

import javax.swing.*;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Arrays;
import java.util.Map;

/**
 * @author Artem Kovalov on 18.05.2023
 */
public final class LoginService {
    private LoginService() {
    }
    private static final int INTERVAL_SESSION = 30 * 60;
    private static final Map<String, JPasswordField> userBase = Map.of(
            "admin", new JPasswordField("adminPass"),
            "user", new JPasswordField("userPass")
    );
    public static void setUserSession(HttpServletRequest request, HttpServletResponse response)
            throws IOException {
        PrintWriter out = response.getWriter();
        String userParam = "user";

        // Это значение наших параметров
        String user = request.getParameter(userParam);
        String pwd = request.getParameter("pwd");

        if (isPasswordMatches(user, pwd)) {
            HttpSession session = request.getSession();
            session.setAttribute(userParam, userParam);
            //setting session to expiry in 30 mins (value of 'intervalSession')
            setSessionInterval(session, response, user);

            out.println("Welcome back to the team, " + user + "!");
        } else {
            out.println("Either user name or password is wrong!");
        }
    }
    public static boolean isUserExists(String userName) {
        return userName != null && userBase.containsKey(userName);
    }
    public static boolean isPasswordMatches(String userName, String password) throws BadRequestException {
        if (isUserExists(userName) && (password != null)) {
            JPasswordField passwordField = userBase.get(userName);
            char[] fieldPassword = passwordField.getPassword();
            return Arrays.equals(password.toCharArray(), fieldPassword);
        }
        return false;
    }
    private static void setSessionInterval(HttpSession session, HttpServletResponse response, String user) {
        session.setMaxInactiveInterval(INTERVAL_SESSION);
        Cookie userName = new Cookie("user", user);
        userName.setMaxAge(INTERVAL_SESSION);
        response.addCookie(userName);
    }
}
