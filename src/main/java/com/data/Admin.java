package com.data;

import java.sql.*;
import java.util.ArrayList;
import java.util.Scanner;

public class Admin extends Users{
    int id;
    String salt, hash;
    public ArrayList<Doctor> Doctors = new ArrayList<>();

    String dbName = "Hospital";
    String dbuser = "root";
    String dbpassword = "sqltest";
    String baseURL = "jdbc:mysql://localhost:3306/";
    String fullURL = baseURL + dbName + "?serverTimezone=UTC";

    public Admin() {}

//    public Admin(String username, int password, String name, String surname){
//        super(username, password, name, surname);
//    }

    public Admin(int id, String username, int password, String name, String surname){
        super(username, password, name, surname);
        this.id = id;
    }

    public Admin(String username, int password, String name, String surname, String salt, String hash){
        super(username, password, name, surname);
        setSalt(salt);
        setHash(hash);
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

    public void insertIndb(){
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)) {
            String insertPat = "INSERT INTO admins (username, password, name, surname, salt, hash) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertPat);
            pstmt.setString(1, this.username);
            pstmt.setInt(2, this.password);
            pstmt.setString(3, this.name);
            pstmt.setString(4, this.surname);
            pstmt.setString(5, this.salt);
            pstmt.setString(6, this.hash);
            pstmt.executeUpdate();
            System.out.println("Admin inserted.");
            pstmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    public void deleteFromdb(String dbName){
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword);
            Statement stmt = conn.createStatement()) {
            String deleteApp = "DELETE FROM admins WHERE `username` = " + this.username;
            stmt.executeUpdate(deleteApp);
            System.out.println("Admin deleted.");
            stmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    public Doctor AddDoctors(String username, int password, String name, String surname, String speciality, String salt, String hash) {
        Doctor doctor = new Doctor(username, password, name, surname, speciality, salt, hash);
        Doctors.add(doctor);
        doctor.insertIndb();
        return doctor;
    }

    public void DeleteDoctor(Doctor d) {
        d.deleteFromdb();
    }

    public Doctor getDoctor(ArrayList<Doctor> doctors){
        Doctor doc = null;
        for (int i = 0; i < doctors.size(); i++) {
            System.out.println(doctors.get(i).toString());
            char response;
            do {
                System.out.println("Is this the doctor you are looking for? Y/N");
                response = (new Scanner(System.in)).next().toUpperCase().charAt(0);
            }while (response != 'Y' && response != 'N' );
            if (response == 'Y') {
                doc = doctors.get(i);
            }
        }
        return doc;
    }

    @Override
    public String toString(){
        return String.format("Admin ID: %d, Name: %s, Surname: %s", id, name, surname);
    }
}