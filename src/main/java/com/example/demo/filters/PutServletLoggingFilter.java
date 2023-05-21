package com.example.demo.filters;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.util.Enumeration;
import java.util.StringJoiner;

/**
 * Servlet Filter implementation class RequestLoggingFilter
 */
@WebFilter("/putServlet")
public class PutServletLoggingFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log(">>> PutServletLoggingFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain) throws IOException, ServletException {
        HttpServletRequest req = (HttpServletRequest) request;

        logUserRequest(req);
        // pass the request along the filter chain
        chain.doFilter(request, response);
    }

    private void logUserRequest(HttpServletRequest req) {
        HttpSession session = req.getSession();
        Object user = session.getAttribute("user");
        String userName = user != null ? (String) user : "Anonymous";

        String requestURI = req.getRequestURI();

        this.context.log("User:[" + userName + "] called [" + requestURI + "] with these params:");

        Enumeration<String> params = req.getParameterNames();
        StringJoiner sj = new StringJoiner(";", "[", "]");

        while (params.hasMoreElements()) {
            String name = params.nextElement();
            String value = req.getParameter(name);
            sj.add('{' + name + '=' + value + '}');
        }
        this.context.log(sj.toString());
    }
}

