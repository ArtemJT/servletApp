package com.example.demo.filters;

import com.example.demo.constant.PermittedURLPath;
import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;

@WebFilter("/*")
public class AuthenticationFilter implements Filter {

    private ServletContext context;

    @Override
    public void init(FilterConfig fConfig) throws ServletException {
        this.context = fConfig.getServletContext();
        this.context.log(">>> AuthenticationFilter initialized");
    }

    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest req = (HttpServletRequest) request;
        HttpServletResponse res = (HttpServletResponse) response;

        String uri = req.getRequestURI();

        this.context.log("Requested Resource::http://localhost:8080" + uri);

        HttpSession session = req.getSession(false);

        checkAccess(request, response, chain, res, uri, session);
    }

    @Override
    public void destroy() {
        //close any resources here
    }

    private void checkAccess(ServletRequest request, ServletResponse response,
                             FilterChain chain, HttpServletResponse res,
                             String uri, HttpSession session) throws IOException, ServletException {

        boolean isLoggedIn = session != null && session.getAttribute("user") != null;

        if (isLoggedIn || PermittedURLPath.getAllPaths().contains(uri)) {
            chain.doFilter(request, response);
        } else {
            this.context.log("<<< Unauthorized access request");
            PrintWriter out = res.getWriter();
            out.println("No access!!!");
            out.close();
            res.sendRedirect("loginServlet");
        }
    }
}
