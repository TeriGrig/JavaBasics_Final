package com.data;

import java.io.PrintWriter;
import java.sql.*;

import jakarta.servlet.*;
import jakarta.servlet.http.*;
import jakarta.servlet.annotation.*;

import java.io.IOException;

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

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession();

        Users u = new Users();
        u.setUsername(username);
        u.setPassword(Integer.parseInt(password));
        session = u.login(session);

        if (session != null) {
            String storedHash = (String) session.getAttribute("hash");
            String saltBase = (String) session.getAttribute("salt");
            String table = (String) session.getAttribute("table");

            try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);) {
                PasswordUtils passwordUtils = new PasswordUtils();
                //byte[] salt = Base64.getDecoder().decode(saltBase64);
                String hashedInput = passwordUtils.hashPassword(Integer.parseInt(password), saltBase);

                if (hashedInput.equals(storedHash)) {
                    // Login successful

                    String sql = "SELECT * FROM " + table + " WHERE username = ?";
                    PreparedStatement stmt = conn.prepareStatement(sql);
                    stmt.setString(1, username);

                    ResultSet rs = stmt.executeQuery();

                    session.setAttribute("table", table);
                    if (rs.next()) {
                        session.setAttribute("id", rs.getInt("id"));
                        session.setAttribute("username", rs.getString("username"));
                        session.setAttribute("name", rs.getString("name"));
                        session.setAttribute("surname", rs.getString("surname"));
                        if (table == "patients") {
                            session.setAttribute("AMKA", rs.getInt("AMKA"));
                        }else if (table == "doctors"){
                            session.setAttribute("speciality", rs.getString("speciality"));
                        }
                    }
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

                    while (rs2.next()) {
                        Appointment a = new Appointment(
                                rs2.getInt("doctor_id"),
                                rs2.getInt("patient_id"),
                                rs2.getString("date").charAt(0),
                                rs2.getString("time")
                        );
                        session.setAttribute("doc", a.getDocinfo());
                        session.setAttribute("day", a.getDay(rs2.getString("date").charAt(0)));
                        session.setAttribute("time", a.getTime());
                    }

                    rs2.close();
                    stmt2.close();

                    RequestDispatcher rd = request.getRequestDispatcher("info/index.jsp");
                    rd.forward(request, response);
                } else {
                    // Login failed
                    response.sendRedirect("index.jsp");
                }
            } catch (Exception e) {
                e.printStackTrace();
                response.sendRedirect("index.jsp");
                out.println("<h2>Error: " + e.getMessage() + "</h2>");
            }
        } else {
            response.sendRedirect("login.jsp");
        }
    }

//    protected void doGet(HttpServletRequest request, HttpServletResponse response)
//            throws ServletException, IOException {
//
//        response.getWriter().println("Servlet OK!");
//
//        String dbName = "Hospital";
//        String dbuser = "terez";
//        String dbpassword = "sqltest";
//        String baseURL = "jdbc:mysql://localhost:3306/hospital";
//        String fullURL = baseURL + dbName + "?serverTimezone=UTC";
//
//        try {
//            Connection conn = getConnection(baseURL, dbuser, dbpassword);
//
//            Statement stmt = conn.createStatement();
//            ResultSet rs = stmt.executeQuery("SELECT * FROM patients");
//
//            while (rs.next()) {
//                String username = rs.getString("Username");
//                String pass = rs.getString("Password");
//                System.out.println("Username: " + username + ", Pass: " + pass);
//            }
//
//            // Κλείσιμο πόρων
//            rs.close();
//            stmt.close();
//            conn.close();
//
//        } catch (Exception e) {
//            System.out.println("❌ Σφάλμα:");
//            e.printStackTrace();
//        }
//    }
}
