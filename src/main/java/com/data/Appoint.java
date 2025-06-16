package com.data;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/SetDates")
public class Appoint extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{\"error\":\"No valid session or username\"}");
            return;
        }
        String username = (String) session.getAttribute("username");
        String role = (String) request.getParameter("role");
        int day =Integer.parseInt(request.getParameter("day"));
        int id = 0;
        String usernameDoc = null;

        response.setContentType("text/plain");
        response.getWriter().write("Role: " + role + ", Day: " + day);

        try(Connection conn = DBConnection.getConnection()){
            String sql = "SELECT a.id, a.username " +
                    "FROM availableDays a " +
                    "JOIN doctors d ON a.username = d.username " +
                    "WHERE a.availableDay = ? AND a.available = TRUE AND d.speciality = ? " +
                    "LIMIT 1";
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setInt(1, day);
                pstmt.setString(2, role);

                ResultSet rs = pstmt.executeQuery();

                if (rs.next()) {
                    id = rs.getInt("id");
                    usernameDoc = rs.getString("username");
                }
            }catch (Exception e){}

            sql = "INSERT INTO appointments0 (patient_username, doctor_username, day, active) VALUES (?, ?, ?, ?);";
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, username);
                pstmt.setString(2, usernameDoc);
                pstmt.setInt(3, day);
                pstmt.setBoolean(4, true);
                pstmt.executeUpdate();
            }catch (Exception e){}
            sql = "update availableDays set available = ? where id = ?";
            try {
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setBoolean(1, false);
                pstmt.setInt(2, id);
                pstmt.executeUpdate();
            }catch (Exception e){}

        }catch (Exception e){}

//        try(Connection conn = DBConnection.getConnection()) {
//            String sql = "SELECT p.id AS patient_id, d.id AS doctor_id " +
//                    "FROM patient p " +
//                    "JOIN doctor d ON d.speciality = ? " +
//                    "JOIN availableDays a ON a.username = d.username " +
//                    "WHERE p.username = ? AND a.day = ?";
//
//            PreparedStatement pstmt = conn.prepareStatement(sql);
//            pstmt.setString(1, role);       // d.speciality
//            pstmt.setString(2, username);   // p.username
//            pstmt.setInt(3, day);           // a.day
//
//            ResultSet rs = pstmt.executeQuery();
//            int patientId = 0, doctorId = 0;
//            while (rs.next()) {
//                patientId = rs.getInt("patient_id");
//                doctorId = rs.getInt("doctor_id");
//                Appointment ap = new Appointment(doctorId, patientId, day);
//                ap.insertIndb();
//            }
//
//            rs.close();
//            pstmt.close();
//
//        }catch (Exception e) {}
    }
}