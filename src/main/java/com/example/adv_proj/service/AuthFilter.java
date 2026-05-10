package com.example.adv_proj.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;

import jakarta.servlet.*;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebFilter("/*")
public class AuthFilter implements Filter {

    @Override
    public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest request = (HttpServletRequest) req;
        HttpServletResponse response = (HttpServletResponse) res;
        String path = request.getRequestURI().substring(request.getContextPath().length());

        if (isPublicPath(path)) {
            chain.doFilter(request, response);
            return;
        }

        String user = resolveUser(request);
        if (user == null || user.isBlank()) {
            response.sendRedirect(request.getContextPath() + "/login.jsp");
            return;
        }

        request.setAttribute("user", user);
        try {
            request.setAttribute("isAdmin", UserDao.isAdmin(user));
        } catch (Exception e) {
            request.setAttribute("isAdmin", false);
        }

        chain.doFilter(request, response);
    }

    private String resolveUser(HttpServletRequest request) {
        Cookie[] cookies = request.getCookies();

        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("SESSION_ID".equals(cookie.getName())) {
                    String user = SessionDao.getSessionUser(cookie.getValue());
                    if (user != null) {
                        return user;
                    }
                }
            }
        }

        String token = request.getParameter("token");
        if (token == null) {
            token = getCookieValue(cookies, "AUTH_TOKEN");
        }
        if (token == null) {
            String authorization = request.getHeader("Authorization");
            if (authorization != null && authorization.startsWith("Bearer ")) {
                token = authorization.substring(7);
            }
        }

        if (token == null) {
            return null;
        }

        try {
            DecodedJWT jwt = JWT.require(Algorithm.HMAC256("secret"))
                    .build()
                    .verify(token);
            return jwt.getClaim("user").asString();
        } catch (Exception e) {
            return null;
        }
    }

    private boolean isPublicPath(String path) {
        return path.equals("/")
                || path.endsWith("login.jsp")
            || path.endsWith("register.jsp")
            || path.endsWith("invalid-user.jsp")
                || path.endsWith("register.jsp")
                || path.endsWith("/login")
                || path.endsWith("/register")
                || path.endsWith("/logout")
                || path.endsWith(".css")
                || path.endsWith(".js")
                || path.endsWith(".png")
                || path.endsWith(".jpg")
                || path.endsWith(".jpeg")
                || path.endsWith(".svg");
    }

    private String getCookieValue(Cookie[] cookies, String name) {
        if (cookies == null) {
            return null;
        }

        for (Cookie cookie : cookies) {
            if (name.equals(cookie.getName())) {
                return cookie.getValue();
            }
        }
        return null;
    }
}
