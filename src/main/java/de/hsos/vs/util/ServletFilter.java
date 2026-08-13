package de.hsos.vs.util;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;

/**
 * Quelle: Oechsle 2022 - Parallele und verteilte anwendungen in java s.445
 * Zugriffskontrolle
 * "Ein Filter kann allerdings auch die Ausführung der weiteren Filter und damit auch des eigentlichen
 * Servlets unterbinden. " bei dem Projekt wird es manchmal auf /login redirected statt UNAUTHORIZED abzusenden.
 *
 * @author Stanislav
 */
@WebFilter("/api/*")
public class ServletFilter implements Filter {
    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {

        HttpServletRequest req =  (HttpServletRequest) servletRequest;
        HttpServletResponse resp = (HttpServletResponse) servletResponse;


        HttpSession session = req.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            resp.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            return;
        }
        filterChain.doFilter(req, resp);
    }
}
