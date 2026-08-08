//package de.hsos.vs.security;
//
//import jakarta.servlet.*;
//import jakarta.servlet.annotation.WebFilter;
//import jakarta.servlet.annotation.WebServlet;
//import jakarta.servlet.http.HttpServletRequest;
//import jakarta.servlet.http.HttpServletResponse;
//import jakarta.servlet.http.HttpSession;
//
//import java.io.IOException;
//import java.io.PrintWriter;
//
//@WebFilter("/api/*")
//public class ServletFilter implements Filter {
//
//    /**
//     * Wenn der  unauthorisierte User versucht auf die Daten zuzugreifen, dann wird ein Fehler aufgetreten
//     * Stanislav
//     */
//    @Override
//    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
//
//        HttpServletRequest req =  (HttpServletRequest) servletRequest;
//        HttpServletResponse resp = (HttpServletResponse) servletResponse;
//
//
//        HttpSession session = req.getSession(false);
//        if (session == null || session.getAttribute("username") == null) {
//            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
//            return;
//        }
//        filterChain.doFilter(req, resp);
//    }
//}
