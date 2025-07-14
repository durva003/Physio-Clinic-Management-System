import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/GetUserReports")
public class GetUserReports extends HttpServlet {

    // Database connection details
    private static final String DB_URL = "jdbc:mysql://localhost:3306/physio";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // Do not create a new session if one doesn't exist
        String patientUsername = null; // Use a distinct variable name for clarity
        String patientFullName = null;

        // Set content type before writing anything
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath(); // Get the context path for robust linking

        // --- Start HTML Structure ---
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Your Uploaded Reports</title>");
        // Link to the external CSS file using context path
        out.println("    <link rel='stylesheet' href='viewreport.css'>");
        out.println("</head>");
        out.println("<body>");

        // --- Header Bar (consistent theme) ---
        out.println("<div class='header-bar'>");
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Your Uploaded Reports</h1>");
        out.println("  </div>");
        out.println("</div>");

        // --- Main Content Container ---
        out.println("<div class='container'>");
        out.println("    <h2>Your Medical Reports</h2>");

        if (session == null || session.getAttribute("username") == null || session.getAttribute("patientName") == null) {
            out.println("<p class='error-message'>You must be logged in to view reports. Please <a href='" + contextPath + "/patientlogin.html'>Login here</a>.</p>");
            out.println("</div>"); // Close container
            out.println("</body></html>");
            return; // Stop execution if not logged in
        }

        patientUsername = (String) session.getAttribute("username");
        patientFullName = (String) session.getAttribute("patientName"); // Assuming patientName is also stored in session after login

        Connection con = null;
        PreparedStatement pstmt = null; // Use PreparedStatement for security
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // Fetch reports for the patientFullName (more reliable if stored in session)
            String queryReports = "SELECT report_date, description, file_name FROM reports WHERE patient_name = ? ORDER BY report_date DESC";
            pstmt = con.prepareStatement(queryReports);
            pstmt.setString(1, patientFullName); // Bind patientFullName to the query

            rs = pstmt.executeQuery();

            boolean found = false;

            out.println("<div class='table-responsive'>"); // For table responsiveness
            out.println("<table class='reports-table'>"); // Apply theme table class
            out.println("<thead>");
            out.println("<tr>");
            out.println("<th>Report Date</th>");
            out.println("<th>Description</th>");
            out.println("<th>File Name</th>");
            out.println("<th>Action</th>");
            out.println("</tr>");
            out.println("</thead>");
            out.println("<tbody>");

            while (rs.next()) {
                found = true;
                String reportDate = safe(rs.getString("report_date"));
                String description = safe(rs.getString("description"));
                String fileName = safe(rs.getString("file_name")); // Get the file name from DB

                out.println("<tr>");
                out.println("<td>" + reportDate + "</td>");
                out.println("<td>" + description + "</td>");
                out.println("<td>" + fileName + "</td>"); // Display file name
                // Link to download the file (assuming files are in UPLOAD_DIR)
                out.println("<td><a href='" + contextPath + "/uploads/" + fileName + "' target='_blank'>View PDF</a></td>");
                out.println("</tr>");
            }

            out.println("</tbody>");
            out.println("</table>");
            out.println("</div>"); // Close table-responsive

            if (!found) {
                out.println("<p class='info-message'>No reports found for " + patientFullName + ".</p>");
            }

        } catch (SQLException se) {
            System.err.println("Database error loading reports: " + se.getMessage());
            se.printStackTrace();
            out.println("<p class='error-message'>Error loading reports. Please try again later. (DB Error)</p>");
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            out.println("<p class='error-message'>Server configuration error. Please contact support. (Driver Error)</p>");
        } finally {
            // Close resources properly
            try { if (rs != null) rs.close(); } catch (SQLException se) { /* Log or ignore */ }
            try { if (pstmt != null) pstmt.close(); } catch (SQLException se) { /* Log or ignore */ }
            try { if (con != null) con.close(); } catch (SQLException se) { /* Log or ignore */ }
        }

        // --- Buttons (consistent theme) ---
        out.println("<div class='form-actions'>");
        out.println("    <button type='button' onclick=\"window.location.href='" + contextPath + "/report.html'\" class='button secondary-button'>Upload New Report</button>");
        out.println("    <button type='button' onclick=\"window.location.href='" + contextPath + "/PatientDashboard'\" class='button secondary-button'>Back to Dashboard</button>");
        out.println("</div>");

        out.println("</div>"); // Close container

        // --- Footer (consistent theme) ---
        out.println("<div class='footer'>");
        out.println("&copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }

    private String safe(String val) {
        return (val == null || val.trim().isEmpty() || val.equalsIgnoreCase("null")) ? "-" : val;
    }
}
