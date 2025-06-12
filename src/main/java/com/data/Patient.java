package com.data;

import java.sql.*;

public class Patient extends com.data.Users {
    int AMKA, id;
    String salt, hash;

    String baseURL = "jdbc:mysql://localhost:3306/";
    String dbName = "hospital";
    String dbuser = "root";
    String dbpassword = "sqltest";
    String fullURL = baseURL + dbName + "?serverTimezone=UTC";

    public Patient(){};

//    public Patient(String username, int password, String name, String surname, int AMKA){
//        super(username, password, name, surname);
//        setAMKA(AMKA);
//    }

    public Patient(int id, String username, int password, String name, String surname, int AMKA){
        super(username, password, name, surname);
        setAMKA(AMKA);
        this.id = id;
    }

    public Patient(String username, int password, String name, String surname, int AMKA, String salt, String hash){
        super(username, password, name, surname);
        setAMKA(AMKA);
        setSalt(salt);
        setHash(hash);
    }

    public int getId() {return id;}
    public void setId(int id) {
        try {
            this.id = id;
        }catch (Exception e){
            System.out.println(e.getMessage());
        }
    }

    public int getAMKA() {
        return AMKA;
    }
    public void setAMKA(int AMKA) {
        try {
            this.AMKA = AMKA;
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

    public void registration(){}
    public void searchForSpeciality(){}
    public void searchForDoctor(){}
    public void viewProgrammedAppointments(){}
    public void programAppointment(){}
    public void cancelAppointment(){}

    public void insertIndb(){
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)) {
            String insertPat = "INSERT INTO patients (username, password, name, surname, AMKA, salt, hash) VALUES (?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertPat);
            pstmt.setString(1, this.username);
            pstmt.setInt(2, this.password);
            pstmt.setString(3, this.name);
            pstmt.setString(4, this.surname);
            pstmt.setInt(5, this.AMKA);
            pstmt.setString(6, this.salt);
            pstmt.setString(7, this.hash);
            pstmt.executeUpdate();
            System.out.println("Patient inserted.");
            pstmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    public void deleteFromdb(String dbName){
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword);
            Statement stmt = conn.createStatement()) {
            String deleteApp = "DELETE FROM patients WHERE `username` = " + this.username;
            stmt.executeUpdate(deleteApp);
            System.out.println("Patient deleted.");
            stmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    @Override
    public String toString(){
        return String.format("Patient ID: %d, Name: %s, Surname: %s, AMKA: %d", id, name, surname, AMKA);
    }
}
