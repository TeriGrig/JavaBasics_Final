package com.data;

import java.sql.*;
import java.util.ArrayList;

public class Doctor extends com.data.Users {
    public String speciality;
    String salt, hash;

//    String baseURL = "jdbc:mysql://localhost:3306/";
//    String dbName = "hospital";
//    String dbuser = "root";
//    String dbpassword = "sqltest";
//    String fullURL = baseURL + dbName + "?serverTimezone=UTC";

    public Doctor(){};

//    public Doctor(String username, int password, String name, String surname, String speciality) {
//        super(username, password, name, surname);
//        this.speciality = speciality;
//    }

    public Doctor(String username) {this.username =  username;}

    public Doctor(int id, String username, int password, String name, String surname, String speciality) {
        super(username, password, name, surname);
        this.setId(id);
        this.speciality = speciality;
    }

    public Doctor(String username, int password, String name, String surname, String speciality, String salt, String hash){
        super(username, password, name, surname);
        setSpeciality(speciality);
        setSalt(salt);
        setHash(hash);
    }

    public String getSpeciality() {
        return speciality;
    }
    public void setSpeciality(String speciality) {
        try {
            this.speciality = speciality;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public String getSalt(){return salt;}
    public void setSalt(String salt){
        try {
            this.salt = salt;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }}
    public String getHash(){return hash;}
    public void setHash(String hash){
        try {
            this.hash = hash;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void availability(){}
    public void viewProgrammedAppointments(){

    }
    public void cancelAppointment(){}

    public void insertIndb(){
        try(Connection conn = DBConnection.getConnection();) {
            String insertDoc = "INSERT INTO doctors (username, password, name, surname, speciality, salt, hash) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertDoc);
            pstmt.setString(1, this.username);
            pstmt.setInt(2, this.password);
            pstmt.setString(3, this.name);
            pstmt.setString(4, this.surname);
            pstmt.setString(5, this.speciality);
            pstmt.setString(6, this.salt);
            pstmt.setString(7, this.hash);
            pstmt.executeUpdate();
            System.out.println("Doctor inserted.");
            pstmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    public void deleteFromdb(){
        try(Connection conn = DBConnection.getConnection();) {
            String deleteApp = "DELETE FROM doctors WHERE username = ?";
            PreparedStatement pstmt = conn.prepareStatement(deleteApp);
            pstmt.setString(1, this.username);
            //pstmt.setInt(2, this.password);
            pstmt.executeUpdate();
            System.out.println("Doctor deleted.");
            pstmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    @Override
    public String toString() {
        return String.format("Doctor ID %d, Name: %s, Surname: %s, Speciality: %s", id, name, surname, speciality);
    }
}