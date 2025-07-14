import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/PatientDetails")
public class PatientDetails extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String username = request.getParameter("username");
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("<meta charset='UTF-8'>");
        out.println("<title>Patient Details - " + username + "</title>");
        out.println("<link rel='stylesheet' href='patientdetails.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='header-bar'>");
        out.println("  <div class='back-button-container'>");
        out.println("    <a href='viewpatients' class='back-button'>&#8592; Back</a>");
        out.println("  </div>");
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Patient Details</h1>");
        out.println("  </div>");
        out.println("</div>");

        out.println("<div class='container'>");
        out.println("<h2>History for: " + username + "</h2>");

        if (username == null || username.trim().isEmpty()) {
            out.println("<p class='error-message'>Error: Username parameter is missing.</p>");
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
                Statement stmt = con.createStatement();

                // Personal Info
                String query1 = "SELECT * FROM reg WHERE username = '" + username + "'";
                ResultSet rs1 = stmt.executeQuery(query1);

                String patientFullName = null;

                out.println("<div class='section'>");
                out.println("<h3>Personal Information</h3>");
                if (rs1.next()) {
                    patientFullName = rs1.getString("patientName");
                    out.println("<p><strong>Name:</strong> " + patientFullName + "</p>");
                    out.println("<p><strong>Age:</strong> " + rs1.getInt("age") + "</p>");
                    out.println("<p><strong>Gender:</strong> " + rs1.getString("gender") + "</p>");
                    out.println("<p><strong>Contact:</strong> " + rs1.getString("contact") + "</p>");
                    out.println("<p><strong>Email:</strong> " + rs1.getString("email") + "</p>");
                } else {
                    out.println("<p class='no-data-message'>No personal information found.</p>");
                }
                rs1.close();
                out.println("</div>");

                // Appointments
                String query2 = "SELECT * FROM appointment WHERE patientName = '" + username + "' ORDER BY appointmentDate DESC";
                ResultSet rs2 = stmt.executeQuery(query2);

                out.println("<div class='section'>");
                out.println("<h3>Appointments</h3>");
                if (!rs2.isBeforeFirst()) {
                    out.println("<p class='no-data-message'>No appointments found.</p>");
                } else {
                    out.println("<ul>");
                    while (rs2.next()) {
                        int appointmentId = rs2.getInt("id");
                        out.println("<li>");
                        out.println("<strong>ID:</strong> " + appointmentId + "<br>");
                        out.println("<strong>Date:</strong> " + rs2.getDate("appointmentDate") + "<br>");
                        out.println("<strong>Reason:</strong> " + rs2.getString("reason"));

                        // Prescription updates
                        Statement stmt3 = con.createStatement();
                        String query3 = "SELECT * FROM appointment_updates WHERE appointment_id = " + appointmentId + " ORDER BY prescription_date DESC";
                        ResultSet rs3 = stmt3.executeQuery(query3);

                        if (rs3.isBeforeFirst()) {
                            out.println("<ul>");
                            while (rs3.next()) {
                                out.println("<li>");
                                out.println("<b>Prescription Date:</b> " + rs3.getDate("prescription_date") + "<br>");
                                out.println("<b>Medication:</b> " + rs3.getString("medication") + "<br>");
                                out.println("<b>Notes:</b> " + rs3.getString("notes"));
                                out.println("</li>");
                            }
                            out.println("</ul>");
                        } else {
                            out.println("<p class='no-data-message' style='margin: 5px 0 0 0; font-size: 0.9em;'>No prescriptions for this appointment.</p>");
                        }

                        rs3.close();
                        stmt3.close();
                        out.println("</li>");
                    }
                    out.println("</ul>");
                }
                rs2.close();
                out.println("</div>");

                if (patientFullName != null) {
                    Statement stmt4 = con.createStatement();
                    String query4 = "SELECT * FROM reports WHERE patient_name = '" + patientFullName + "' ORDER BY report_date DESC";
                    ResultSet rs4 = stmt4.executeQuery(query4);

                    out.println("<div class='section'>");
                    out.println("<h3>Medical Reports</h3>");

                    if (!rs4.isBeforeFirst()) {
                        out.println("<p class='no-data-message'>No reports found.</p>");
                    } else {
                        out.println("<ul>");
                        while (rs4.next()) {
                            String fileName = rs4.getString("file_name");
                            String reportLink = request.getContextPath() + "/uploads/" + fileName;

                            out.println("<li>");
                            out.println("<strong>Date:</strong> " + rs4.getDate("report_date") + "<br>");
                            out.println("<strong>Description:</strong> " + rs4.getString("description") + "<br>");
                            out.println("<a href='" + reportLink + "' target='_blank'>View Report</a>");
                            out.println("</li>");
                        }
                        out.println("</ul>");
                    }

                    rs4.close();
                    stmt4.close();
                    out.println("</div>");
                } else {
                    out.println("<div class='section'>");
                    out.println("<h3>Medical Reports</h3>");
                    out.println("<p class='no-data-message'>Patient details not found. Cannot fetch reports.</p>");
                    out.println("</div>");
                }

                stmt.close();
                con.close();

            } catch (Exception e) {
                out.println("<p class='error-message'>Error: " + e.getMessage() + "</p>");
                e.printStackTrace();
            }
        }

        out.println("</div>");
        out.println("<div class='footer'> &copy; 2025 Physiorehab Admin Panel </div>");
        out.println("</body>");
        out.println("</html>");
        out.close();
    }
}
