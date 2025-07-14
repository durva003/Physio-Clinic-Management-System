import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/viewprescriptions")
public class viewprescriptions extends HttpServlet {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/physio";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String patientName = (session != null) ? (String) session.getAttribute("patientName") : null;

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>"); // Added doctype
        out.println("<html><head><title>View Prescriptions</title>");
        out.println("<link rel='stylesheet' href='prescriptions.css'>"); // Link to external CSS
        out.println("</head><body>");

   
        out.println("<div class='header-bar'>");
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Your Prescriptions</h1>"); // Changed title for this page
        out.println("  </div>");
        out.println("</div>");
        // --- End Header Bar ---

        out.println("<div class='container'>"); // Main content container for styling
        out.println("<h2>Prescription Details</h2>"); // This H2 is inside the container

        if (patientName == null || patientName.trim().isEmpty()) {
            out.println("<p class='error-message'>You must be logged in to view prescriptions. Please <a href='patientlogin.html'>Login here</a>.</p>");
        } else {
            // Added message for no data found, outside the table to be clearer
            boolean dataFound = false; // Initialize here

            // Start table only if data might be found or to show 'no records' row
            out.println("<div class='table-responsive'>"); // For better table scrolling on small screens
            out.println("<table class='prescription-table'>"); // Added class for styling
            out.println("<thead><tr>"); // Use thead for semantic markup
            out.println("<th>Patient Name</th>");
            out.println("<th>Appointment Date</th>");
            out.println("<th>Prescription Date</th>");
            out.println("<th>Medication</th>");
            out.println("<th>Notes</th>");
            out.println("<th>Exercises</th>");
            out.println("</tr></thead>");
            out.println("<tbody>"); // Use tbody for semantic markup

            Connection conn = null;
            Statement stmt = null;
            ResultSet rs = null;

            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);
                stmt = conn.createStatement();
                // SQL query to fetch prescriptions for the logged-in patient
                String sql = "SELECT a.patientName, a.appointmentDate, au.prescription_date, au.medication, au.notes, au.exercises " +
                             "FROM appointment a JOIN appointment_updates au ON a.id = au.appointment_id " +
                             "WHERE a.patientName = '" + patientName + "' ORDER BY a.appointmentDate DESC, au.prescription_date DESC"; // Added JOIN and ORDER BY for better results

                rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    dataFound = true;
                    out.println("<tr>");
                    out.println("<td>" + safe(rs.getString("patientName")) + "</td>");
                    out.println("<td>" + safe(rs.getString("appointmentDate")) + "</td>");
                    out.println("<td>" + safe(rs.getString("prescription_date")) + "</td>");
                    out.println("<td>" + safe(rs.getString("medication")) + "</td>");
                    out.println("<td>" + safe(rs.getString("notes")) + "</td>");
                    out.println("<td>" + safe(rs.getString("exercises")) + "</td>");
                    out.println("</tr>");
                }

            } catch (SQLException se) {
                out.println("<p class='error-message'>Database Error: " + se.getMessage() + "</p>");
                se.printStackTrace();
            } catch (ClassNotFoundException e) {
                out.println("<p class='error-message'>Driver not found: " + e.getMessage() + "</p>");
                e.printStackTrace();
            } finally {
                // Close resources properly
                try { if (rs != null) rs.close(); } catch (SQLException se) { se.printStackTrace(); }
                try { if (stmt != null) stmt.close(); } catch (SQLException se) { se.printStackTrace(); }
                try { if (conn != null) conn.close(); } catch (SQLException se) { se.printStackTrace(); }
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>"); // End table-responsive

            if (!dataFound) {
                out.println("<p class='info-message'>No prescription records found for you.</p>"); // Use a distinct class for info messages
            }
        }

        out.println("<div class='form-actions'>"); // Use the same button container style
        out.println("<button type='button' onclick=\"window.location.href='PatientDashboard'\" class='button secondary-button'>Back to Dashboard</button>");
        out.println("</div>");

        out.println("</div>"); // End container

        out.println("<div class='footer'>");
        out.println("&copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }

    private String safe(String val) {
        return (val == null || val.trim().isEmpty() || val.equalsIgnoreCase("null")) ? "-" : val; // Use "-" instead of "" for better readability
    }
}