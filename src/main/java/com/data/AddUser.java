package com.data;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;

@WebServlet("/AddUser")
public class AddUser extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String name = request.getParameter("name").toUpperCase();
        String surname = request.getParameter("surname").toUpperCase();
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String property = request.getParameter("property");

        PasswordUtils passwordUtils = new PasswordUtils();
        String salt = passwordUtils.generateSalt();
        String hash = passwordUtils.hashPassword(Integer.parseInt(password), salt);

        if (property.equals("Patient")) {
            String AMKA = request.getParameter("AMKA");
            Patient p = new Patient(username, Integer.parseInt(password), name, surname, Integer.parseInt(AMKA), salt, hash);
            p.insertIndb();
        }else if (property.equals("Doctor")) {
            String doctor_type = request.getParameter("doctor-type").toUpperCase();
            Doctor d = new Doctor(username, Integer.parseInt(password), name, surname, doctor_type, salt, hash);
            d.insertIndb();
        }

        response.sendRedirect("info/index.jsp?addSucc=true");
    }
}