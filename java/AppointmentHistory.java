import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/AppointmentHistory")
public class AppointmentHistory extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String patientName = (session != null) ? (String) session.getAttribute("patientName") : null;

        response.setContentType("text/html;charset=UTF-8"); 
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath();

        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Appointment History</title>");
        out.println("    <link rel='stylesheet' href='appointmenthistory.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("<div class='header-bar'>");
        out.println("  <a href='" + contextPath + "/PatientDashboard' class='header-back-button'>< Back to Dashboard</a>");
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Appointment History</h1>");
        out.println("  </div>");
        out.println("</div>");

        out.println("<div class='container'>");

        if (patientName == null || patientName.trim().isEmpty()) {
            out.println("<p class='info-message' style='color:red;'>You must be logged in to view appointment history.</p>");
        } else {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
                Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");

               
                String sql = "SELECT appointmentDate, appointmentTime, reason FROM appointment WHERE patientName = ?";
                PreparedStatement pstmt = conn.prepareStatement(sql);
                pstmt.setString(1, patientName); // Set patientName parameter

                ResultSet rs = pstmt.executeQuery();

                out.println("<div class='table-responsive'>"); // Wrapper for responsive table
                out.println("<table class='reports-table'>"); // Use consistent table class
                out.println("<thead><tr><th>Date</th><th>Time</th><th>Reason</th></tr></thead>");
                out.println("<tbody>");

                boolean hasRecords = false;
                while (rs.next()) {
                    hasRecords = true;
                    out.println("<tr>");
                    out.println("<td>" + rs.getString("appointmentDate") + "</td>");
                    out.println("<td>" + rs.getString("appointmentTime") + "</td>");
                    out.println("<td>" + rs.getString("reason") + "</td>");
                    out.println("</tr>");
                }
                if (!hasRecords) {
                    out.println("<tr><td colspan='3' class='no-records-message'>No appointments found.</td></tr>");
                }
                out.println("</tbody>");
                out.println("</table>");
                out.println("</div>"); // Close table-responsive

                rs.close();
                pstmt.close(); // Close PreparedStatement
                conn.close();

            } catch (Exception e) {
                out.println("<p class='error-message'>Error retrieving appointments: " + e.getMessage() + "</p>");
            }
        }


        out.println("</div>");

        out.println("<div class='footer'>");
        out.println("© 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }
}
