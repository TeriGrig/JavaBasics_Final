package com.data;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;

@WebServlet("/DeleteUser")
public class DeleteUser  extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String id = request.getParameter("id");
        String role = request.getParameter("role");

        try(Connection conn = DBConnection.getConnection();){
            String deleteApp = "";
            if (!"patient".equals(role)) {
                deleteApp = "DELETE FROM doctors WHERE id = ?";
            }
            else {
                deleteApp = "DELETE FROM patients WHERE id = ?";
            }
            PreparedStatement pstmt = conn.prepareStatement(deleteApp);
            pstmt.setInt(1, Integer.parseInt(id));
            pstmt.executeUpdate();
            pstmt.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
        response.setContentType("text/plain");
        response.getWriter().write("Delete ID: " + id);
    }
}
