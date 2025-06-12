package com.data;
import jakarta.servlet.http.HttpSession;

import java.sql.*;
import java.util.ArrayList;

import static java.lang.System.out;

public class Users {
    String username, name, surname;
    int password, usersCounter;

    String dbName = "hospital";
    String dbuser = "root";
    String dbpassword = "sqltest";
    String baseURL = "jdbc:mysql://localhost:3306/";
    String fullURL = baseURL + dbName + "?serverTimezone=UTC";

    String[] tables = new String[]{"admins", "doctors", "patients"};

    public Users(){}

    public Users(String username, int password, String name, String surname) {
        setUsername(username);
        setPassword(password);
        setName(name);
        setSurname(surname);
    }

    public String getUsername() {return username;}
    public int getPassword() {return password;}
    public String getName() {return name;}
    public String getSurname() {return surname;}

    public void setUsername(String username) {
        try {
            this.username = username;
        }catch(Exception e){
            out.println(e.getMessage());
        }
    }
    public void setPassword(int password) {
        try {
            this.password = password;
        }catch(Exception e){
            out.println(e.getMessage());
        }
    }
    public void setName(String name) {
        try {
            this.name = name;
        }catch(Exception e){
            out.println(e.getMessage());
        }
    }
    public void setSurname(String surname) {
        try {
            this.surname = surname;
        }catch(Exception e){
            out.println(e.getMessage());
        }
    }

    public HttpSession login(HttpSession session) {
        try {
            // JDBC σύνδεση
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword);
            String storedHash = null;
            String saltBase = null;
            String table = null;
            boolean check = false;
            int i = 0;
            do {
                // Retrieve stored hash and salt from DB using the username
                String tablename = tables[i];
                String s1 = "SELECT hash, salt FROM " + tablename + " WHERE username = ?";
                PreparedStatement ps = conn.prepareStatement(s1);
                ps.setString(1, username);  // ασφαλές: αποφεύγει SQL injection
                ResultSet rs1 = ps.executeQuery();
                if (rs1.next()) {
                    storedHash = rs1.getString("hash");
                    saltBase = rs1.getString("salt");
                    table = tablename;
                    check = true;

                    session.setAttribute("hash", storedHash);
                    session.setAttribute("salt", saltBase);
                    session.setAttribute("table", table);

                    out.println("<h2>" + storedHash + "<br>" + saltBase + "</h2>");
                }
                i++;
            } while ((i < tables.length) && (!check));
            if (!check) {
                out.println("Ο χρήστης δεν βρέθηκε");
            }
        } catch (Exception e) {
            out.println("<h2>Error: " + e.getMessage() + "</h2>");
        } finally {
            out.close();
        }
        return session;
    }

    public ArrayList<Admin> selectAdminFromDb(String text) {
        ArrayList<Admin> admins = new ArrayList<>();
        Admin a = null;
        ResultSet rs = null;
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)){
            String selectQuery = text;
            PreparedStatement pstm = conn.prepareStatement(selectQuery);
            rs = pstm.executeQuery();
            while (rs.next()) {
                a = new Admin(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("password"),
                        rs.getString("name"),
                        rs.getString("surname")
                );
                admins.add(a);
            }
            rs.close();
            pstm.close();
            conn.close();
        }catch(Exception e){
            out.println("Error working with table: " + e.getMessage());
        }
        return admins;
    }

    public ArrayList<Patient> selectPatientFromDb(String text) {
        ArrayList<Patient> patients = new ArrayList<>();
        Patient p = null;
        ResultSet rs = null;
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)){
            String selectQuery = text;
            PreparedStatement pstm = conn.prepareStatement(selectQuery);
            rs = pstm.executeQuery();
            while (rs.next()) {
                p = new Patient(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getInt("AMKA")
                );
                patients.add(p);
            }
            rs.close();
            pstm.close();
            conn.close();
        }catch(Exception e){
            out.println("Error working with table: " + e.getMessage());
        }
        return patients;
    }

    public ArrayList<Doctor> selectDoctorFromDb(String text) {
        ArrayList<Doctor> docs = new ArrayList<>();
        Doctor d = null;
        ResultSet rs = null;
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)){
            String selectQuery = text;
            PreparedStatement pstm = conn.prepareStatement(selectQuery);
            rs = pstm.executeQuery();
            while (rs.next()) {
                d = new Doctor(
                        rs.getInt("id"),
                        rs.getString("username"),
                        rs.getInt("password"),
                        rs.getString("name"),
                        rs.getString("surname"),
                        rs.getString("speciality")
                );
                docs.add(d);
            }
            rs.close();
            pstm.close();
            conn.close();
        }catch(Exception e){
            out.println("Error working with table: " + e.getMessage());
        }
        return docs;
    }

    public ArrayList<Appointment> selectAppFromDb(String text){
        ArrayList<Appointment> appointments = new ArrayList<>();
        Appointment a = null;
        ResultSet rs = null;
        try (Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)){
            String selectQuery = text;
            PreparedStatement ptsm = conn.prepareStatement(selectQuery);
            rs = ptsm.executeQuery();
            while (rs.next()){
                a = new Appointment(
                        rs.getInt(1),
                        rs.getInt(2),
                        rs.getString(3).charAt(0),
                        rs.getString(4)
                );
                appointments.add(a);
            }
            rs.close();
            ptsm.close();
            conn.close();
        }catch (Exception e){
            out.println("Error working with table: " + e.getMessage());
        }
        return appointments;
    }

    public void logout(){

    }
}