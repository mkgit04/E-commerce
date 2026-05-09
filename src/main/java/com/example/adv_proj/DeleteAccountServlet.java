package com.example.adv_proj;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/delete-account")
public class DeleteAccountServlet extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = (String) request.getAttribute("user");

        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in");
            return;
        }

        try {
            AppDao.deleteUser(user);
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("SESSION_ID".equals(cookie.getName())) {
                        AppDao.deleteSession(cookie.getValue());
                    }
                }
            }
            clearCookie(response, "SESSION_ID");
            clearCookie(response, "AUTH_TOKEN");
            response.sendRedirect("login.jsp");
        } catch (Exception e) {
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, e.getMessage());
        }
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
