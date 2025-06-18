<%@ page import="java.util.ArrayList" %>
<%@ page import="com.data.Appointment" %>
<%@ page import="com.data.Users" %>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@ page import="com.data.Users" %>
<%@ page import="com.data.Appointment" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="java.time.format.TextStyle" %>
<%@ page import="java.util.Locale" %>

<%
    LocalDate today = LocalDate.now();
    String month = today.getMonth().getDisplayName(TextStyle.FULL, new Locale("el", "GR"));
    String category = null;
    String username = null;
    String surname = null;
    String name = null;
    String speciality = null;
    int AMKA = 0;
    ArrayList<Appointment> appointments = null;
    ArrayList<Users> users = null;
    Users user = (Users) session.getAttribute("current_user");
    if (session != null) {
        username = (String) session.getAttribute("username");
        name = (String) session.getAttribute("name");
        surname = (String) session.getAttribute("surname");
        category = (String) session.getAttribute("table");
        if (category != null) {
            if (category.equals("doctors")) {
                speciality = (String) session.getAttribute("speciality");
            }
            if (category.equals("patients")) {
                AMKA = (int) session.getAttribute("AMKA");
            }
            if (category.equals("doctors") || category.equals("patients")) {
                appointments = (ArrayList<Appointment>) session.getAttribute("appointments");
                if (appointments == null){
                    appointments = new ArrayList<>();
                }
            }
            if (category.equals("admins")) {
                users = (ArrayList<Users>) session.getAttribute("users");
                if (users == null){
                    users = new ArrayList<>();
                }
            }

        }
    } else {
        System.out.println("Session is null");
    }
%>

<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <link rel="stylesheet" href="<%=request.getContextPath()%>/info/style.css">
    <title>List</title>
</head>
<body data-category="<%=category%>" data-username="<%=username%>">
<div class="box">
    <form method="POST" action="logout" id="logout">
        <% if (category.equals("doctors")) {%>
        <img src="<%=request.getContextPath()%>/info/imgs/doctors.svg" alt="rule" class="rule" onclick="document.getElementById('logout').submit()">
        <%} else if (category.equals("patients")) {%>
        <img src="<%=request.getContextPath()%>/info/imgs/patients.svg" alt="rule" class="rule" onclick="document.getElementById('logout').submit()">
        <%} else if (category.equals("admins")) {%>
        <img src="<%=request.getContextPath()%>/info/imgs/admins.svg" alt="rule" class="rule" onclick="document.getElementById('logout').submit()">
        <%}%>
    </form>

    <div class="inside" id="inside">
        <div id="color" class="color"></div>
        <div class="name"><h1><%=username%></h1></div>
        <div class="descreption"><p>SURNAME: <%=surname%><br>NAME: <%=name%>
            <%if (category.equals("doctors")) {%>
            <br>SPECIALITY: <%=speciality%><br>
            <%} else if (category.equals("patients")) {%>
            <br>AMKA: <%=AMKA%><br>
            <%}%>
        </p></div>

        <div class="icons">
            <div class="icon" onclick="addFunc()" title="Add to appointments">
                <%
                    if (category.equals("doctors") || category.equals("patients")) {%>
                <svg viewBox="0 0 16 16" fill="currentColor" xmlns="http://www.w3.org/2000/svg" aria-hidden="true">
                    <path fill-rule="evenodd" d="m 6.5 0 c -3.578125 0 -6.5 2.921875 -6.5 6.5 s 2.921875 6.5 6.5 6.5 c 0.167969 0 0.335938 -0.007812 0.5 -0.019531 v -2.007813 c -0.164062 0.019532 -0.332031 0.027344 -0.5 0.027344 c -2.496094 0 -4.5 -2.003906 -4.5 -4.5 s 2.003906 -4.5 4.5 -4.5 s 4.5 2.003906 4.5 4.5 c 0 0.167969 -0.007812 0.335938 -0.027344 0.5 h 2.007813 c 0.011719 -0.164062 0.019531 -0.332031 0.019531 -0.5 c 0 -3.578125 -2.921875 -6.5 -6.5 -6.5 z m 0 3 c -0.277344 0 -0.5 0.222656 -0.5 0.5 v 2.5 h -1.5 c -0.277344 0 -0.5 0.222656 -0.5 0.5 s 0.222656 0.5 0.5 0.5 h 2 c 0.277344 0 0.5 -0.222656 0.5 -0.5 v -3 c 0 -0.277344 -0.222656 -0.5 -0.5 -0.5 z m 4.5 5 v 3 h -3 v 2 h 3 v 3 h 2 v -3 h 3 v -2 h -3 v -3 z m 0 0"/>
                </svg>
                <% } else if (category.equals("admins")) {%>
                <svg fill="currentColor" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512" xml:space="preserve"><g><g><path d="M300.434,257.599c-25.945,27.304-60.622,43.875-98.602,43.875c-37.979,0-72.656-16.571-98.602-43.875c-45.617,28.738-77.826,76.818-85.092,132.736c-1.659,12.77,8.291,24.107,21.201,24.107h225.846c0-53.371,32.011-99.402,77.838-119.914C330.812,280.165,316.452,267.69,300.434,257.599z"/></g></g><g><g><ellipse cx="201.828" cy="133.868" rx="112.229" ry="133.868"/></g></g><g><g><path d="M396.486,316.885c-53.794,0-97.558,43.764-97.558,97.558S342.693,512,396.486,512c53.792,0,97.557-43.764,97.557-97.558S450.279,316.885,396.486,316.885z M435.199,431.315h-21.841v21.841c0,9.318-7.554,16.872-16.872,16.872c-9.318,0-16.872-7.554-16.872-16.872v-21.841h-21.842c-9.318,0-16.872-7.554-16.872-16.872c0-9.319,7.554-16.872,16.872-16.872h21.842v-21.841c0-9.318,7.554-16.872,16.872-16.872c9.318,0,16.872,7.554,16.872,16.872v21.841h21.841c9.318,0,16.872,7.554,16.872,16.872C452.072,423.761,444.518,431.315,435.199,431.315z"/></g></g>
                        </svg>
                <% }
                %>
            </div>
            <div class="icon" onclick="changeColor()">
                <svg class="w-6 h-6 text-gray-800 dark:text-white" aria-hidden="true" xmlns="http://www.w3.org/2000/svg" fill="currentColor" viewBox="0 0 24 24">
                    <path fill-rule="evenodd" d="M11.32 6.176H5c-1.105 0-2 .949-2 2.118v10.588C3 20.052 3.895 21 5 21h11c1.105 0 2-.948 2-2.118v-7.75l-3.914 4.144A2.46 2.46 0 0 1 12.81 16l-2.681.568c-1.75.37-3.292-1.263-2.942-3.115l.536-2.839c.097-.512.335-.983.684-1.352l2.914-3.086Z" clip-rule="evenodd"/>
                    <path fill-rule="evenodd" d="M19.846 4.318a2.148 2.148 0 0 0-.437-.692 2.014 2.014 0 0 0-.654-.463 1.92 1.92 0 0 0-1.544 0 2.014 2.014 0 0 0-.654.463l-.546.578 2.852 3.02.546-.579a2.14 2.14 0 0 0 .437-.692 2.244 2.244 0 0 0 0-1.635ZM17.45 8.721 14.597 5.7 9.82 10.76a.54.54 0 0 0-.137.27l-.536 2.84c-.07.37.239.696.588.622l2.682-.567a.492.492 0 0 0 .255-.145l4.778-5.06Z" clip-rule="evenodd"/>
                </svg>

            </div>
            <div class="icon" onclick="openApp()">
                <%
                    if (category.equals("doctors") || category.equals("patients")) {%>
                <svg viewBox="0 0 16 16" xmlns="http://www.w3.org/2000/svg" fill="currentColor">
                    <path fill-rule="evenodd" d="M6 0L5 .25V1h1zm0 1v1H5V1h-.006c-1.259.014-2.18-.03-2.932.384-.376.208-.675.56-.84.999C1.058 2.821 1 3.343 1 4v8c0 .657.058 1.178.222 1.617.165.439.464.788.84.996.753.415 1.674.372 2.932.387h6.012c1.258-.015 2.178.028 2.931-.387a1.87 1.87 0 0 0 .838-.996c.165-.439.225-.99.225-1.617V4c0-.658-.06-1.179-.225-1.617a1.88 1.88 0 0 0-.838-.998c-.753-.416-1.673-.37-2.931-.385H11v1h-1V1zm4 0h1V0l-1 .25zM5.006 5H11c1.26.014 2.087.06 2.453.261.183.102.289.213.386.473.098.26.16 1.266.16 1.266v5c0 .592-.062 1.005-.16 1.265-.097.26-.203.372-.386.473-.366.202-1.194.247-2.453.262H5c-1.26-.015-2.087-.06-2.454-.262-.183-.101-.289-.213-.386-.473C2.062 13.005 2 12.592 2 12V7c0-.593.062-1.006.16-1.266.097-.26.203-.371.386-.473.367-.202 1.196-.247 2.46-.261z" overflow="visible"  white-space="normal"/>
                </svg>
                <% } else if (category.equals("admins")) {%>
                <svg fill="currentColor" xmlns="http://www.w3.org/2000/svg"xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 512 512" space="preserve"><g><g><path d="M300.435,257.599c-25.944,27.303-60.62,43.876-98.602,43.876c-37.976,0-72.654-16.568-98.602-43.876c-45.617,28.739-77.825,76.818-85.092,132.735c-1.659,12.77,8.291,24.108,21.201,24.108h225.844c0.001-53.371,32.012-99.402,77.841-119.914C330.813,280.165,316.452,267.69,300.435,257.599z"/></g></g><g><g><ellipse cx="201.835" cy="133.862" rx="112.233" ry="133.862"/></g></g><g><g><path d="M396.486,316.885c-53.794,0-97.557,43.764-97.557,97.557S342.692,512,396.486,512s97.557-43.764,97.557-97.557S450.279,316.885,396.486,316.885zM435.199,431.315h-77.427c-9.318,0-16.872-7.554-16.872-16.872c0-9.318,7.554-16.872,16.872-16.872h77.427c9.318,0,16.872,7.554,16.872,16.872C452.071,423.761,444.518,431.315,435.199,431.315z"/></g></g>
                </svg>
                <% }
                %>
            </div>

        </div>
    </div>
    <div class="appointments" id="appointments">
            <div class="menu">
                <div class="titleMenu">
                    <h1><%if("doctors".equals(category)){%> Patient <% } else if("patients".equals(category)) { %> Doctor <%} else {%> Users <%}%></h1>
                    <h1>Date</h1>
                </div>
                <div class="menuDetails">
<%--                    <div class="details" tabindex="-1">--%>
<%--                            <h2>--NAME--</h2>--%>
<%--                        <h2>--DAY--</h2>--%>
<%--                    </div>--%>
                </div>
            </div>
<%--                    <div class="details" tabindex="-1">--%>
<%--                        <input type="hidden" name="id" value="<%=u.getId()%>">--%>
<%--                        <input type="hidden" name="category" value="<%=u.getRole()%>">--%>
<%--                        <h3><%=u.getFullName()%></h3>--%>
<%--                        <h3><%=u.getRole()%></h3>--%>
<%--                    </div>--%>

        <div class="buttons">
            <svg onclick="closeApp()" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M16.19 2H7.81C4.17 2 2 4.17 2 7.81V16.18C2 19.83 4.17 22 7.81 22H16.18C19.82 22 21.99 19.83 21.99 16.19V7.81C22 4.17 19.83 2 16.19 2ZM13.92 16.13H9C8.59 16.13 8.25 15.79 8.25 15.38C8.25 14.97 8.59 14.63 9 14.63H13.92C15.2 14.63 16.25 13.59 16.25 12.3C16.25 11.01 15.21 9.97 13.92 9.97H8.85L9.11 10.23C9.4 10.53 9.4 11 9.1 11.3C8.95 11.45 8.76 11.52 8.57 11.52C8.38 11.52 8.19 11.45 8.04 11.3L6.47 9.72C6.18 9.43 6.18 8.95 6.47 8.66L8.04 7.09C8.33 6.8 8.81 6.8 9.1 7.09C9.39 7.38 9.39 7.86 9.1 8.15L8.77 8.48H13.92C16.03 8.48 17.75 10.2 17.75 12.31C17.75 14.42 16.03 16.13 13.92 16.13Z" fill="#292D32"/>
            </svg>
            <svg onclick="Delete()" xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 430 430"><path fill="#c71f16" d="m334.47 82.81-2 38.89-1.15 22.8-12.1 237.13a10 10 0 0 1-3.104 6.743 10 10 0 0 1-6.896 2.747H120.76a10 10 0 0 1-10-9.49L98.68 144.5l-1.17-22.8-2-38.89a2 2 0 0 1 2-2h234.96a2 2 0 0 1 2 2"/><path stroke="#ebe6ef" stroke-linecap="round" stroke-linejoin="round" stroke-width="10" d="M156.46 172.689v155.91m58.67-155.91v155.91m58.41-155.91v155.91"/><path stroke="#c71f16" stroke-linecap="round" stroke-linejoin="round" stroke-width="10" d="M259.16 79.7c0-24.26-22.56-41.85-43.75-41.85a45 45 0 0 0-44.57 38.74v5.84h88.32z"/><path fill="#c71f16" d="M342.71 75.8H87.3c-5.523 0-10 4.477-10 10v25.9c0 5.523 4.477 10 10 10h255.41c5.523 0 10-4.477 10-10V85.8c0-5.523-4.477-10-10-10"/><path fill="#c71f16" d="m332.49 121.699-1.17 22.83H98.68l-1.17-22.83z" opacity=".6" style="mix-blend-mode:multiply"/>
            </svg>
        </div>
    </div>

    <div class="add" id="add">
        <%
            //        Doctor
            if (category.equals("doctors")) {%>
        <div class="calendar">
            <div class="titleCalendar">
                <h1><%=month%></h1>
            </div>
            <div class="calendarDay">
                <%
                    String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                    int dayNumber = 1;
                    int dayIndex = 0;

                    while (dayNumber <= 31) {
                        String dayName = days[dayIndex % 7]; // Επαναφορά μετά την Κυριακή
                %>
                <div class="day">
                    <input type="hidden" name="day" value="<%= dayNumber %>">
                    <h2><%=dayName%> </h2>
                    <h3> <%=dayNumber%> </h3>
                </div>
                <%
                        dayNumber++;
                        dayIndex++;
                    }%>
            </div></div>
        <%}
        %>
        <!-- Admin -->
        <%
            if (category.equals("admins")) {%>
        <form method="Post" action="AddUser" class="forma" id="addUser">
            <h1>Add user</h1>
            <div class="inps">
                <div class="input-group">
                    <label for="name">Name</label>
                    <input type="text" id="name" name="name" required>
                </div>

                <div class="input-group">
                    <label for="surname">Surname</label>
                    <input type="text" id="surname" name="surname" required>
                </div>

                <div class="input-group">
                    <label for="username">Username</label>
                    <input type="text" id="username" name="username" required>
                </div>

                <div class="input-group">
                    <label for="password">Password</label>
                    <input type="password" id="password" name="password" required>
                </div>

                <div class="input-group">
                    <label for="property">Property</label>
                    <select name="property" id="property" onchange = "showOptions()" required>
                        <option value="">--Select--</option>
                        <option value="Doctor">Doctor</option>
                        <option value="Patient">Patient</option>
                    </select>
                </div>

                <div class="input-group hidden" id = "patient-options">
                    <label for="AMKA">AMKA</label>
                    <input type="text" id="AMKA" name="AMKA" required>
                </div>

                <div class="input-group hidden" id = "doctor-options">
                    <label for="speciality">Speciality</label>
                    <select name="doctor-type" id="doctor-type" required>
                        <option value="">-- Select Speciality --</option>
                        <option value="dentist">Dentist</option>
                        <option value="pathologist">Pathologist</option>
                        <option value="cardiologist">Cardiologist</option>
                        <option value="pediatrician">Pediatrician</option>
                        <option value="orthopedic">Orthopedic</option>
                        <option value="dermatologist">Dermatologist</option>
                        <option value="psychiatrist">Psychiatrist</option>
                        <option value="gynecologist">Gynecologist</option>
                        <option value="neurologist">Neurologist</option>
                        <option value="surgeon">Surgeon</option>
                        <option value="ophthalmologist">Opthalmologist</option>
                    </select>
                </div>

            </div>
        </form>
        <% }

//        Patient
        else if (category.equals("patients")) {%>
        <div class="setDate">
            <h1>Set Doctor Date</h1>
            <div class="form">
                <label for="doctor-type">Doctor Type</label>
                <select name="doctor-type" id="doctor-type">
                    <option value="">-- Select Speciality --</option>
                    <option value="dentist">Dentist</option>
                    <option value="pathologist">Pathologist</option>
                    <option value="cardiologist">Cardiologist</option>
                    <option value="pediatrician">Pediatrician</option>
                    <option value="orthopedic">Orthopedic</option>
                    <option value="dermatologist">Dermatologist</option>
                    <option value="psychiatrist">Psychiatrist</option>
                    <option value="gynecologist">Gynecologist</option>
                    <option value="neurologist">Neurologist</option>
                    <option value="surgeon">Surgeon</option>
                    <option value="ophthalmologist">Opthalmologist</option>
                </select>
<%--                --%>
                <div class="calendar" id="calendar">
                    <div class="titleCalendar">
                        <h1><%=month%></h1>
                    </div>
                    <div class="calendarDay">
                        <%
                            String[] days = {"Mon", "Tue", "Wed", "Thu", "Fri", "Sat", "Sun"};
                            int dayNumber = 1;
                            int dayIndex = 0;

                            while (dayNumber <= 31) {
                                String dayName = days[dayIndex % 7]; // Επαναφορά μετά την Κυριακή
                        %>
                        <div class="day">
                            <input type="hidden" name="day" value="<%= dayNumber %>">
                            <h2><%=dayName%> </h2>
                            <h3> <%=dayNumber%> </h3>
                        </div>
                        <%
                                dayNumber++;
                                dayIndex++;
                            }%>
                    </div></div>
<%--                --%>
            </div>
        </div>
        <% } %>
        <div class="buttons">
            <svg onclick="closeAdd()" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
                <path d="M16.19 2H7.81C4.17 2 2 4.17 2 7.81V16.18C2 19.83 4.17 22 7.81 22H16.18C19.82 22 21.99 19.83 21.99 16.19V7.81C22 4.17 19.83 2 16.19 2ZM13.92 16.13H9C8.59 16.13 8.25 15.79 8.25 15.38C8.25 14.97 8.59 14.63 9 14.63H13.92C15.2 14.63 16.25 13.59 16.25 12.3C16.25 11.01 15.21 9.97 13.92 9.97H8.85L9.11 10.23C9.4 10.53 9.4 11 9.1 11.3C8.95 11.45 8.76 11.52 8.57 11.52C8.38 11.52 8.19 11.45 8.04 11.3L6.47 9.72C6.18 9.43 6.18 8.95 6.47 8.66L8.04 7.09C8.33 6.8 8.81 6.8 9.1 7.09C9.39 7.38 9.39 7.86 9.1 8.15L8.77 8.48H13.92C16.03 8.48 17.75 10.2 17.75 12.31C17.75 14.42 16.03 16.13 13.92 16.13Z" fill="#292D32"/>
            </svg>
            <svg onclick="Save()" viewBox="0 0 32 32" xmlns="http://www.w3.org/2000/svg"><defs><style>.cls-1{fill:#04009a;}.cls-2{fill:#77acf1;}</style></defs><g data-name="13. Floppy Disk" id="_13._Floppy_Disk"><path class="cls-1" d="M27,32H25a1,1,0,0,1,0-2h2a1,1,0,0,0,1-1V7.829a1,1,0,0,0-.293-.708L22.879,2.293A1.009,1.009,0,0,0,22.172,2H5A1,1,0,0,0,4,3V14a1,1,0,0,1-2,0V3A3,3,0,0,1,5,0H22.172a2.978,2.978,0,0,1,2.121.879l4.828,4.828A2.983,2.983,0,0,1,30,7.829V29A3,3,0,0,1,27,32Z"/><path class="cls-1" d="M21,32H5a3,3,0,0,1-3-3V18a1,1,0,0,1,2,0V29a1,1,0,0,0,1,1H21a1,1,0,0,1,0,2Z"/><path class="cls-1" d="M19,10H9A3,3,0,0,1,6,7V1A1,1,0,0,1,7,0H21a1,1,0,0,1,1,1V7A3,3,0,0,1,19,10ZM8,2V7A1,1,0,0,0,9,8H19a1,1,0,0,0,1-1V2Z"/><path class="cls-1" d="M21,32H7a1,1,0,0,1-1-1V21a3,3,0,0,1,3-3H19a3,3,0,0,1,3,3V31A1,1,0,0,1,21,32ZM8,30H20V21a1,1,0,0,0-1-1H9a1,1,0,0,0-1,1Z"/><path class="cls-2" d="M17,6H16a1,1,0,0,1,0-2h1a1,1,0,0,1,0,2Z"/><path class="cls-2" d="M17,24H11a1,1,0,0,1,0-2h6a1,1,0,0,1,0,2Z"/><path class="cls-2" d="M17,28H11a1,1,0,0,1,0-2h6a1,1,0,0,1,0,2Z"/></g>
            </svg>
        </div>
    </div>

    <img src="<%=request.getContextPath()%>/info/imgs/user.png" alt="User Image" class="user-image" id="userImage">
</div>

<%
    String a = request.getParameter("addSucc");
    if ("true".equals(a)) { // Ασφαλές null-safe check
%>
<div class="verify" id="verify">
    <img src="<%=request.getContextPath()%>/info/imgs/verified.gif" alt="Image">
</div>
<%
    }
%>
<script src="<%=request.getContextPath()%>/info/script.js"></script>
</body>
</html>