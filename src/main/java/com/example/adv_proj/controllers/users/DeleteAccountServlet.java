package com.example.adv_proj.controllers.users;

import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.example.adv_proj.service.SessionDao;
import com.example.adv_proj.service.UserDao;

@WebServlet("/delete-account")
public class DeleteAccountServlet extends HttpServlet {

    private static final Logger LOGGER = Logger.getLogger(DeleteAccountServlet.class.getName());

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String user = (String) request.getAttribute("user");

        if (user == null || user.isBlank()) {
            response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Not signed in");
            return;
        }

        try {
            UserDao.deleteUser(user);
            Cookie[] cookies = request.getCookies();
            if (cookies != null) {
                for (Cookie cookie : cookies) {
                    if ("SESSION_ID".equals(cookie.getName())) {
                        SessionDao.deleteSession(cookie.getValue());
                    }
                }
            }
            clearCookie(response, "SESSION_ID");
            clearCookie(response, "AUTH_TOKEN");
            response.sendRedirect(request.getContextPath() + "/login.jsp");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Failed to delete account for user " + user, e);
            response.sendError(HttpServletResponse.SC_INTERNAL_SERVER_ERROR, "Unable to delete account right now");
        }
    }

    private void clearCookie(HttpServletResponse response, String name) {
        Cookie cookie = new Cookie(name, "");
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);
    }
}
