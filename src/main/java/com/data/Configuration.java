package com.data;

import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;
import java.util.ArrayList;

@WebServlet("/login")
public class Configuration extends HttpServlet{

    private static final long serialVersionUID = 1L;

    private final String DB_URL = "jdbc:mysql://localhost:3306/Hospital?useSSL=false&serverTimezone=UTC";
    private final String DB_USER = "root";
    private final String DB_PASS = "sqltest";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        if ((!username.isEmpty()) && (!password.isEmpty())) {
            response.setContentType("text/html;charset=UTF-8");
            PrintWriter out = response.getWriter();

            HttpSession session = request.getSession();

            Users u = new Users();
            u.setUsername(username);
            u.setPassword(Integer.parseInt(password));
            session.setAttribute("current_user", u);
            session = u.login(session);

            if (session != null) {
                String storedHash = (String) session.getAttribute("hash");
                String saltBase = (String) session.getAttribute("salt");
                String table = (String) session.getAttribute("table");

                try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);) {
                    PasswordUtils passwordUtils = new PasswordUtils();
                    String hashedInput = passwordUtils.hashPassword(Integer.parseInt(password), saltBase);

                    if (hashedInput.equals(storedHash)) {
                        // Login successful
                        String sql = "SELECT * FROM " + table + " WHERE username = ?";
                        PreparedStatement stmt = conn.prepareStatement(sql);
                        stmt.setString(1, username);
                        ResultSet rs = stmt.executeQuery();
                        if (rs.next()) {
                            session.setAttribute("id", rs.getInt("id"));
                            session.setAttribute("username", rs.getString("username"));
                            session.setAttribute("password", rs.getString("password"));
                            session.setAttribute("name", rs.getString("name"));
                            session.setAttribute("surname", rs.getString("surname"));

                            if (table == "patients") {
                                session.setAttribute("AMKA", rs.getInt("AMKA"));
                            } else if (table == "doctors") {
                                session.setAttribute("speciality", rs.getString("speciality"));
                            }
                        }
                        if (table.equals("doctors") || table.equals("patients")) {
                            int Id = rs.getInt("id");
                            String column = null;
                            switch (table) {
                                case "doctors":
                                    column = "doctor_id";
                                    break;
                                case "patients":
                                    column = "patient_id";
                                    break;
                            }

                            String sql2 = "SELECT * FROM appointments WHERE " + column + " = ?";
                            PreparedStatement stmt2 = conn.prepareStatement(sql2);
                            stmt2.setInt(1, Id);
                            ResultSet rs2 = stmt2.executeQuery();
                            ArrayList<Appointment> appointments = new ArrayList<>();
                            while (rs2.next()) {
                                Appointment a = new Appointment(
                                        rs2.getInt("doctor_id"),
                                        rs2.getInt("patient_id"),
                                        rs2.getString("date").charAt(0),
                                        rs2.getString("time")
                                );
                                appointments.add(a);
                            }
                            session.setAttribute("appointments", appointments);

                            rs2.close();
                            stmt2.close();
                            rs.close();
                            stmt.close();
                        } else if (table.equals("admins")) {
                            ArrayList<Users> users = new ArrayList<>();

                            String sql1 = "SELECT * FROM doctors";
                            PreparedStatement stmt1 = conn.prepareStatement(sql1);
                            ResultSet rs1 = stmt1.executeQuery();
                            while (rs1.next()) {
                                Users user = new Users(
                                        rs1.getString("name"),
                                        rs1.getString("surname"),
                                        "Doctor"
                                );
                                users.add(user);
                            }
                            rs1.close();
                            stmt1.close();

                            String sql2 = "SELECT * FROM patients";
                            PreparedStatement stmt2 = conn.prepareStatement(sql2);
                            ResultSet rs2 = stmt2.executeQuery();
                            while (rs2.next()) {
                                Users user = new Users(
                                        rs2.getString("name"),
                                        rs2.getString("surname"),
                                        "Patient"
                                );
                                users.add(user);
                            }
                            session.setAttribute("users", users);
                            rs2.close();
                            stmt2.close();
                            for (Users U : users){
                                System.out.println(u.name);
                            }
                        }
                        RequestDispatcher rd = request.getRequestDispatcher("info/index.jsp");
                        rd.forward(request, response);
                    } else {
                        // Login failed
                        response.sendRedirect("index.jsp");
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    response.sendRedirect("index.jsp");
                    System.out.println("Error: " + e.getMessage());
                }
            } else {
                response.sendRedirect("index.jsp");
            }
        }else {
            response.sendRedirect("index.jsp");
        }
    }
}