package com.data;
import java.util.ArrayList;
import java.util.Scanner;
import java.sql.*;

import static java.sql.DriverManager.getConnection;

public class CreateUsers {
    public static void main(String[] args) throws SQLException, ClassNotFoundException {
        String name, surname, speciality;
        String username = null;
        int AMKA, choice, num;
        int password = 0;
        char type;

        String baseURL = "jdbc:mysql://localhost:3306/";
        String dbName = "hospital";
        String dbuser = "root";
        String dbpassword = "sqltest";
        String fullURL = baseURL + dbName + "?serverTimezone=UTC";

        try (Connection conn = getConnection(fullURL, dbuser, dbpassword)) {
            Admin a = null;
            Users u = new Users();
            Doctor d = null;
            Patient p = null;
            do {
                System.out.println("Choose the preferred action.");
                System.out.println("1. Create a new user");
                System.out.println("2. Login");
                System.out.println("3. Exit");
                choice = (new Scanner(System.in)).nextInt();
                switch (choice) {
                    case 1:
                        do {
                            System.out.println("Type A if you are an Admin or P if you are a patient.");
                            type = (new Scanner(System.in)).next().charAt(0);
                        } while ((type != 'A') && (type != 'P'));
                        System.out.println("Enter your name: ");
                        name = (new Scanner(System.in)).next().toUpperCase();
                        System.out.println("Enter your surname: ");
                        surname = (new Scanner(System.in)).next().toUpperCase();
                        do {
                            System.out.println("Enter your username: ");
                            username = (new Scanner(System.in)).next();
                        }while (username.equals(null) || username.equals(""));
                        System.out.println("Enter your password(numbers only): ");
                        Scanner scanner = new Scanner(System.in);
                        while (!scanner.hasNextInt()) {
                            System.out.println("That's not a valid integer. Try again:");
                            scanner.next();
                        }
                        password = scanner.nextInt();
                        PasswordUtils pu = new PasswordUtils();
                        String salt = pu.generateSalt();
                        String hashed = pu.hashPassword(password, salt);
                        if (type == 'A') {
                            a = new Admin(username, password, name, surname, salt, hashed);
                            a.insertIndb();
                        } else if (type == 'P') {
                            System.out.println("Enter your AMKA: ");
                            AMKA = Integer.parseInt(System.console().readLine());
                            p = new Patient(username, password, name, surname, AMKA, salt, hashed);
                            p.insertIndb();
                        }
                        break;
                    case 2:
                        System.out.println("Time to login: ");
                        do {
                            System.out.println("Type A if you are an Anmin, D if you are a Doctor or P if you are Patient.");
                            type = (new Scanner(System.in)).next().charAt(0);
                        } while ((type != 'A') && (type != 'D') && (type != 'P'));
                        String testuser = null;
                        int testpass = 0;
                        do {
                            System.out.println("Give me a username");
                            testuser = System.console().readLine();
                        }while (testuser.equals(null) || testuser.equals(""));
                        System.out.println("Give me a password");
                        Scanner scanner2 = new Scanner(System.in);
                        while (!scanner2.hasNextInt()) {
                            System.out.println("That's not a valid integer. Try again:");
                            scanner2.next();
                        }
                        testpass = scanner2.nextInt();
                        boolean result = false;
                        String storedHash = null;
                        String saltBase64 = null;
                        String table = null;
                        if (type == 'A'){
                            table = "admins";
                        }
                        else if (type == 'P'){
                            table = "patients";
                        } else if (type == 'D'){
                            table = "doctors";
                        }

                        String s = "SELECT * FROM " + table + " WHERE username = ?";
                        PreparedStatement ps3 = conn.prepareStatement(s);
                        ps3.setString(1, testuser);
                        ResultSet rs = ps3.executeQuery();
                        if (rs.next()) {
                            a = new Admin(
                                    rs.getInt("id"),
                                    rs.getString("username"),
                                    rs.getInt("password"),
                                    rs.getString("name"),
                                    rs.getString("surname")
                            );
                            storedHash = String.valueOf(rs.getString("hash"));
                            saltBase64 = String.valueOf(rs.getString("salt"));
                        }

                        try {
                            PasswordUtils passwordUtils = new PasswordUtils();
                            //byte[] salt = Base64.getDecoder().decode(saltBase64);
                            String hashedInput = passwordUtils.hashPassword(testpass, saltBase64);

                            if (hashedInput.equals(storedHash)) {
                                // Login successful
                                System.out.println("Successfully logged in!");
                                result = true;
                            } else {
                                // Login failed
                                System.out.println("Failed to log in!");
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                            System.out.println("Error logging in!");
                        }

                        if (result && type == 'A') {
                            do {
                                System.out.println("Choose the preferred action.");
                                System.out.println("1. Create Doctor.");
                                System.out.println("2. Delete Doctor.");
                                System.out.println("3. Logout");
                                num = (new Scanner(System.in)).nextInt();
                                switch (num){
                                    case 1:
                                        System.out.println("Enter Doctor's name: ");
                                        name = (new Scanner(System.in)).next().toUpperCase();
                                        System.out.println("Enter Doctor's surname: ");
                                        surname = (new Scanner(System.in)).next().toUpperCase();
                                        do {
                                            System.out.println("Enter Doctor's username: ");
                                            username = (new Scanner(System.in)).next();
                                        }while (username.equals(null) || username.equals(""));
                                        System.out.println("Enter Doctor's password(numbers only): ");
                                        Scanner scanner3 = new Scanner(System.in);
                                        while (!scanner3.hasNextInt()) {
                                            System.out.println("That's not a valid integer. Try again:");
                                            scanner3.next();
                                        }
                                        password = scanner3.nextInt();
                                        PasswordUtils pu2 = new PasswordUtils();
                                        String salt2 = pu2.generateSalt();
                                        String hashed2 = pu2.hashPassword(password, salt2);
                                        System.out.println("Enter Doctor's speciality: ");
                                        speciality = (new Scanner(System.in)).next().toUpperCase();
                                        a.AddDoctors(username, password, name, surname, speciality, salt2, hashed2 );
                                        break;
                                    case 2:
                                        Doctor doc = null;
                                        ArrayList<Doctor> doctors = new ArrayList<>();
                                        String s5 = "SELECT * FROM doctors";
                                        doctors = a.selectDoctorFromDb(s5);
                                        System.out.println(String.format("Found %d doctors", doctors.size()));
                                        doc = a.getDoctor(doctors);
                                        a.DeleteDoctor(doc);
                                        break;
                                }
                            }while (num != 3);
                        }
                        else if (result && type == 'D') {
                            ArrayList<Appointment> appointments = new ArrayList<>();
                            do {
                                System.out.println("Choose the preferred action.");
                                System.out.println("1. Edit availability.");
                                System.out.println("2. View programmed appointments.");
                                System.out.println("3. Cancel appointment.");
                                System.out.println("4. Logout");
                                num = (new Scanner(System.in)).nextInt();
                                switch (num) {
                                    case 1:

                                        break;
                                    case 2:
                                        String s3 = "SELECT * FROM appointments INNER JOIN doctors ON appointments.doctor_id=doctors.id ";
                                        appointments = a.selectAppFromDb(s3);
                                        System.out.println(String.format("Found %d appointments", appointments.size()));
                                        for (int i = 0; i < appointments.size(); i++) {
                                            System.out.println(appointments.get(i).toString());
                                        }
                                        break;
                                    case 3:
                                        for (int i = 0; i < appointments.size(); i++) {
                                            System.out.println(appointments.get(i).toString());
                                            char response;
                                            do {
                                                System.out.println("Do you want to cancel this appointment? Y/N");
                                                response = (new Scanner(System.in)).next().toUpperCase().charAt(0);
                                            } while (response != 'Y' && response != 'N');
                                            if (response == 'Y') {
                                                appointments.get(i).deleteFromdb();
                                            } else {
                                                continue;
                                            }
                                        }
                                        break;
                                }
                            } while (num != 4);
                            d = new Doctor();
                            System.out.println("Successfully logged out ");
                        } else if (result && type == 'P') {
                            Doctor doc = null;
                            ArrayList<Doctor> doctors = null;
                            ArrayList<Appointment> appointments = null;
                            do {
                                System.out.println("Choose the preferred action.");
                                System.out.println("1. Search for speciality.");
                                System.out.println("2. Search for doctor.");
                                System.out.println("3. Program appointment.");
                                System.out.println("4. View programmed appointments.");
                                System.out.println("5. Cancel appointment.");
                                System.out.println("6. Logout");
                                num = (new Scanner(System.in)).nextInt();
                                switch (num) {
                                    case 1:
                                        System.out.println("Type the preferred speciality.");
                                        String sp = (new Scanner(System.in)).next().toUpperCase();
                                        String s6 = "SELECT * FROM doctors WHERE speciality = '" + sp + "'";
                                        doctors = a.selectDoctorFromDb(s6);
                                        System.out.println(String.format("Found %d doctors", doctors.size()));
                                        doc = a.getDoctor(doctors);
                                        break;
                                    case 2:
                                        System.out.println("Type the name of the preferred doctor.");
                                        String n = (new Scanner(System.in)).next().toUpperCase();
                                        System.out.println("Type the surname of the preffered doctor.");
                                        String sn = (new Scanner(System.in)).next().toUpperCase();
                                        String s7 = "SELECT * FROM doctors WHERE name = '" + n + "' AND surname = '" + sn + "'";
                                        doctors = a.selectDoctorFromDb(s7);
                                        System.out.println(String.format("Found %d doctors", doctors.size()));
                                        doc = a.getDoctor(doctors);
                                        break;
                                    case 3:
                                        if (doc != null) {
                                            System.out.println("When would you like to program the appointment?");
                                            char date;
                                            do {
                                                System.out.println("Enter M for Monday, T for Tuesday, W for Wednesday, R for Thursday, F for Friday, S for Saturday and N for Sunday.");
                                                date = (new Scanner(System.in)).next().charAt(0);
                                            }while ((date != 'M') && (date != 'T') && (date != 'W') && (date != 'R') && (date != 'F') && (date != 'S') && (date != 'N'));
                                            System.out.println("Enter the time in the format HH:MM");
                                            String time = (new Scanner(System.in)).next();
                                            Appointment app = new Appointment(doc.id, p.id, date, time);
                                            app.insertIndb();
                                            System.out.println(app.toString());
                                        } else {
                                            System.out.println("Please choose doctor first.");
                                        }
                                        break;
                                    case 4:
                                        String s8 = "SELECT * FROM appointments WHERE patient_id = '" + p.id + "'";
                                        appointments = a.selectAppFromDb(s8);
                                        System.out.println(String.format("Found %d appointments", appointments.size()));
                                        for (int i = 0; i < appointments.size(); i++) {
                                            Appointment app1 = new Appointment(
                                                    appointments.get(i).getDoctor_id(),
                                                    appointments.get(i).getPatient_id(),
                                                    appointments.get(i).getDate(),
                                                    appointments.get(i).getTime()
                                            );
                                            System.out.println(app1.toString());
                                        }
                                        break;
                                    case 5:
                                        String s9 = "SELECT * FROM appointments WHERE patient_id = ' " + p.id + "'";
                                        appointments = a.selectAppFromDb(s9);
                                        System.out.println(String.format("Found %d appointments", appointments.size()));
                                        for (int i = 0; i < appointments.size(); i++) {
                                            System.out.println(appointments.get(i).toString());
                                            System.out.println("Do you want to cancel this appointment? Y/N");
                                            char response = (new Scanner(System.in)).next().charAt(0);
                                            if (response == 'Y') {
                                                appointments.get(i).deleteFromdb();
                                            } else {
                                                continue;
                                            }
                                        }
                                        break;
                                }
                            } while (num != 6);
                            p = new Patient();
                            System.out.println("Successfully logged out ");
                        }
                        break;
                    case 3:
                        System.out.println("Exiting...");
                }
            } while (choice != 2);
        }

// create patient from file
        /*String text = "Patient.txt";
        Patient p2;
        ArrayList<String> info = new ArrayList<>();
        try {
            BufferedReader r = new BufferedReader(new FileReader(text));
            String line;
            while ((line = r.readLine()) != null){
                String[] tokens = line.split(":");
                info.add(tokens[1].trim());
            }
            r.close();
            username = info.get(0);
            password = info.get(1);
            name = info.get(2);
            surname = info.get(3);
            AMKA = Integer.parseInt(info.get(4));

            p2 = new Patient(username, password, name, surname, AMKA);

        }catch (Exception e){}*/
    }
}
