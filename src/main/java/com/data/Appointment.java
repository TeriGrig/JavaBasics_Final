package com.data;
import java.sql.*;
import java.util.ArrayList;

public class Appointment {
    int doctor_id;
    int patient_id;
    char date;
    String time;

    String baseURL = "jdbc:mysql://localhost:3306/";
    String dbName = "hospital";
    String dbuser = "root";
    String dbpassword = "sqltest";
    String fullURL = baseURL + dbName + "?serverTimezone=UTC";

    public Appointment (int doctor_id, int patient_id, char date, String time) {
        this.doctor_id = doctor_id;
        this.patient_id = patient_id;
        this.date = date;
        this.time = time;
    }

    public int getDoctor_id() {return doctor_id;}
    public void setDoctor_id(int doctor_id) {
        try {
            this.doctor_id = doctor_id;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public int getPatient_id() {return patient_id;}
    public void setPatient_id(int patient_id) {
        try {
            this.patient_id = patient_id;
        }catch(Exception e){}
    }

    public char getDate() {return date;}
    public void setDate(char date) {
        try {
            this.date = date;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }
    public String getTime() {return time;}
    public void setTime(String time) {
        try {
            this.time = time;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void insertIndb(){
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword)) {
            String insertApp = "INSERT INTO appointments (patient_id, doctor_id, date, time) VALUES (?, ?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertApp);
            pstmt.setInt(1, patient_id);
            pstmt.setInt(2, doctor_id);
            pstmt.setString(3, String.valueOf(date));
            pstmt.setString(4, time);
            pstmt.executeUpdate();
            System.out.println("Appointment inserted.");
            pstmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    public void deleteFromdb(){
        try(Connection conn = DriverManager.getConnection(fullURL, dbuser, dbpassword);
            Statement stmt = conn.createStatement()) {
            String deleteApp = "DELETE FROM appointments WHERE date = " + this.date + " and time = " + this.time;
            stmt.executeUpdate(deleteApp);
            System.out.println("Appointment deleted.");
            stmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    @Override
    public String toString(){
        Admin a = new Admin();
        Doctor d = null;
        Patient p = null;
        String doc_info = "Unknown doctor";
        String pat_info = "Unknown patient";

        doc_info = getDocinfo();

        String s1 = "SELECT * FROM patients WHERE id = " + patient_id;
        ArrayList<Patient> patients = a.selectPatientFromDb(s1);
        if (!patients.isEmpty()) {
            p = patients.get(0);
            if (p != null) {
                System.out.println(p.toString());
                pat_info = p.surname + " " + p.name;
            }
        }
        String day = getDay(date);
        return String.format("Appointment details: Doctor: %s, Patient: %s, Date: %s, Time: %s", doc_info, pat_info, day, time);
    }

    public String getDay(char c){
        String day = null;
        if (c == 'M'){
            day = "Monday";
        }else if (c == 'T'){
            day = "Tuesday";
        }else if (c == 'W'){
            day = "Wednesday";
        }else if (c == 'R'){
            day = "Thursday";
        }else if (c == 'F'){
            day = "Friday";
        }else if (c == 'S'){
            day = "Saturday";
        }else if (c == 'N'){
            day = "Sunday";
        }
        return day;
    }

    public String getDocinfo(){
        Admin a = new Admin();
        Doctor d = null;
        String doc_info = "Unknown doctor";
        String s = "SELECT * FROM doctors WHERE id = " + doctor_id;
        ArrayList<Doctor> docs = a.selectDoctorFromDb(s);
        if (!docs.isEmpty()) {
            d = docs.get(0);
            if (d != null) {
                doc_info = d.surname + " " + d.name + " " + d.speciality;
            }
        }
        return doc_info;
    }
}

