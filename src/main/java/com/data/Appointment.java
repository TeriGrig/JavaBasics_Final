package com.data;
import java.sql.*;
import java.util.ArrayList;

public class Appointment {
    int doctor_id;
    int patient_id;
    int date;

//    String baseURL = "jdbc:mysql://localhost:3306/";
//    String dbName = "hospital";
//    String dbuser = "root";
//    String dbpassword = "sqltest";
//    String fullURL = baseURL + dbName + "?serverTimezone=UTC";

    public Appointment (int doctor_id, int patient_id, int date) {
        this.doctor_id =doctor_id;
        this.patient_id = patient_id;
        this.date = date;
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

    public int getDate() {return date;}
    public void setDate(int date) {
        try {
            this.date = date;
        }catch(Exception e){
            System.out.println(e.getMessage());
        }
    }

    public void insertIndb(){
        try(Connection conn = DBConnection.getConnection();) {
            String insertApp = "INSERT INTO appointments (patient_id, doctor_id, date) VALUES (?, ?, ?)";
            PreparedStatement pstmt = conn.prepareStatement(insertApp);
            pstmt.setInt(1, patient_id);
            pstmt.setInt(2, doctor_id);
            pstmt.setInt(3, date);
            pstmt.executeUpdate();
            System.out.println("Appointment inserted.");
            pstmt.close();
            conn.close();
        }catch(Exception e){
            System.out.println("Error working with table: " + e.getMessage());
        }
    }

    public void deleteFromdb(){
        String deleteApp = "DELETE FROM appointments WHERE date = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(deleteApp)) {

            pstmt.setInt(1, date);

            int rows = pstmt.executeUpdate();

            if (rows > 0) {
                System.out.println("Appointment deleted.");
            } else {
                System.out.println("No appointment found to delete.");
            }

        } catch (Exception e) {
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
        return String.format("Appointment details: Doctor: %s, Patient: %s, Date: %s", doc_info, pat_info);
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

    public String getPatinfo(){
        Admin a = new Admin();
        Patient p = null;
        String pat_info = "Unknown";
        String s = "SELECT * FROM patients WHERE id = " + patient_id;
        ArrayList<Patient> patients = a.selectPatientFromDb(s);
        if (!patients.isEmpty()) {
            p = patients.get(0);
            if (p != null) {
                pat_info = p.surname + " " + p.name;
            }
        }
        return pat_info;
    }
}
