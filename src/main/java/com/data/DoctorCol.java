package com.data;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.util.ArrayList;

@WebServlet("/SetDay")
public class DoctorCol  extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        BufferedReader reader = request.getReader();
        StringBuilder sb = new StringBuilder();
        String line;
        while ((line = reader.readLine()) != null) {
            sb.append(line);
        }
        String json = sb.toString().trim();
        if (json.startsWith("[") && json.endsWith("]")) {
            json = json.substring(1, json.length() - 1);
        }
        ArrayList<Integer> days = new ArrayList<>();
        if (!json.isEmpty()) {
            String[] parts = json.split(",");
            for (String part : parts) {
                int num = Integer.parseInt(part.trim());
                days.add(num);
            }
        }
        response.setContentType("text/plain");
        response.getWriter().write("Received " + days.size() + " numbers.");

        HttpSession session = request.getSession(false);
        String username = (String) session.getAttribute("username");
        try(Connection conn = DBConnection.getConnection();) {
            String sql = "Insert into availableDays (username, availableDay) VALUES (?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(sql);
            for (Integer day : days) {
                pstmt.setString(1, username);
                pstmt.setInt(2, day);
                pstmt.executeUpdate();
            }
                pstmt.close();
        }catch (Exception e){}

    }
}