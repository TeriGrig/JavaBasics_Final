package com.data;

import com.mysql.cj.xdevapi.JsonArray;
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
import java.util.ArrayList;

@WebServlet("/GetDays")
public class GetDays extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("application/json;charset=UTF-8");
        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("username") == null) {
            response.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
            response.getWriter().print("{\"error\":\"No valid session or username\"}");
            return;
        }
        String username = (String) session.getAttribute("username");
        String role = (String) session.getAttribute("table");
        try (Connection conn = DBConnection.getConnection()) {
            String sql = "";
            if ("doctors".equals(role)) {
                sql = "SELECT availableDay FROM availableDays WHERE username = '"+ username +"'";
            }else{
                String findRole = request.getParameter("findRole");
                sql = "SELECT a.availableDay, a.available FROM availableDays a " +
                        "JOIN doctors d ON a.username = d.username " +
                        "WHERE d.speciality = '" + findRole + "'";
            }
            try (PreparedStatement ps = conn.prepareStatement(sql);
                 ResultSet rs = ps.executeQuery()) {

                StringBuilder jsonArr = new StringBuilder();
                jsonArr.append("[");

                boolean first = true;
                while (rs.next()) {
                    if (!first) jsonArr.append(",");
                    jsonArr.append("{");
                    jsonArr.append("\"day\":").append(rs.getInt("availableDay"));
                    if (!"doctors".equals(role)) {
                        jsonArr.append(",");
                        jsonArr.append("\"available\":").append(rs.getBoolean("available"));
                    }
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
        } catch (Exception e) {
            response.setStatus(HttpServletResponse.SC_INTERNAL_SERVER_ERROR);
            response.getWriter().print("{\"error\":\"" + e.getMessage() + "\"}");
        }
    }
}
