package com.data;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;

import java.io.IOException;
import java.io.PrintWriter;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/getApp")
public class GetApp extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{\"error\":\"No valid session or username\"}");
            return;
        }
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("table");
        String n = "";
        if (role == "doctors") {
            n = "patient_username";
        }else {
            n = "doctor_username";
        }
        try (Connection conn = DBConnection.getConnection()) {

            String sql = "select "+ n +", day, active from appointments0 where patient_username = '" + username + "'";

            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                StringBuilder jsonArr = new StringBuilder();
                jsonArr.append("[");

                boolean first = true;
                while (rs.next()) {
                    if (!first) jsonArr.append(",");
                    jsonArr.append("{");
                    jsonArr.append("\"name\":\"").append(rs.getString(n)).append("\",");
                    jsonArr.append("\"day\":").append(rs.getInt("day")).append(",");
                    jsonArr.append("\"active\":").append(rs.getBoolean("active"));
                    jsonArr.append("}");
                    first = false;
                }

                jsonArr.append("]");

                response.setContentType("application/json");
                PrintWriter out = response.getWriter();
                out.print(jsonArr.toString());
                out.flush();
                out.close();
            }
        }catch (Exception e){}
    }
}