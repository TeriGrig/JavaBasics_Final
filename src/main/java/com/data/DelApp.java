package com.data;

import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

@WebServlet("/DelApp")
public class DelApp  extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        String name = request.getParameter("name");
        String date = request.getParameter("date");


        try(Connection conn = DBConnection.getConnection()) {
            String update = "UPDATE availableDays\n" +
                    "SET available = 1\n" +
                    "WHERE username = ? AND availableDay = ?;\n";
            String detele = "DELETE FROM appointments0\n" +
                    "WHERE doctor_username = ? AND day = ?;\n";
            try {
                PreparedStatement pstmt = conn.prepareStatement(update);
                pstmt.setString(1, name);
                pstmt.setInt(2, Integer.parseInt(date));

                pstmt.executeUpdate();
            } catch (Exception e) {
            }
            try {
                PreparedStatement pstmt = conn.prepareStatement(detele);
                pstmt.setString(1, name);
                pstmt.setInt(2, Integer.parseInt(date));

                pstmt.executeUpdate();
            }catch (Exception e){}
            conn.commit();
        }catch (Exception e){}

        response.setContentType("text/plain");
        response.getWriter().write("Delete app: " + name + " " + date);
    }
}
