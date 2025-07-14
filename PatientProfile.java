import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/PatientProfile")
public class PatientProfile extends HttpServlet {

    // Database connection details (consistent with other servlets)
    private static final String DB_URL = "jdbc:mysql://localhost:3306/physio";
    private static final String DB_USER = "root";
    private static final String DB_PASS = "";

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        String fullName = (session != null) ? (String) session.getAttribute("patientName") : null;

        String contextPath = request.getContextPath(); // Get context path for resource linking

        // --- Start HTML Structure ---
        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Patient Profile</title>");
        // Link to the new external CSS file
        out.println("<link rel='stylesheet' href='patientprofile.css'>");
        out.println("</head><body>");

        // --- Header Bar (consistent theme) ---
        out.println("<div class='header-bar'>");
        out.println("  <a href='" + contextPath + "/PatientDashboard' class='header-back-button'>< Back to Dashboard</a>");
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Patient Profile</h1>");
        out.println("  </div>");
        out.println("</div>");

        // --- Main Content Container ---
        out.println("<div class='container'>");
        out.println("<h2>Your Profile</h2>");

        if (username == null || fullName == null) {
            out.println("<p class='error-message'>Please <a href='" + contextPath + "/patientlogin.html'>login</a> first to view your profile.</p>");
            out.println("</div>"); // Close container
            out.println("</body></html>");
            return;
        }

        int age = 0;
        Connection con = null;
        Statement stmt = null; // Changed from PreparedStatement to Statement
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            String query = "SELECT age FROM reg WHERE username = '" + username + "'";
            
            stmt = con.createStatement(); // Changed to createStatement()
            rs = stmt.executeQuery(query); // Changed to executeQuery(query) with concatenated string

            if (rs.next()) {
                age = rs.getInt("age");
            }

        } catch (SQLException se) {
            System.err.println("Database error fetching profile: " + se.getMessage());
            se.printStackTrace(); // Log the full stack trace for debugging
            out.println("<p class='error-message'>Error fetching profile data. Please try again later. (DB Error)</p>");
            out.println("</div>"); // Close container
            out.println("</body></html>");
            return;
        } catch (ClassNotFoundException e) {
            System.err.println("JDBC Driver not found: " + e.getMessage());
            e.printStackTrace();
            out.println("<p class='error-message'>Server configuration error. Please contact support. (Driver Error)</p>");
            out.println("</div>"); // Close container
            out.println("</body></html>");
            return;
        } finally {
            // Close resources in finally block
            try { if (rs != null) rs.close(); } catch (SQLException se) { /* Log or ignore */ }
            try { if (stmt != null) stmt.close(); } catch (SQLException se) { /* Log or ignore */ } // Changed to close stmt
            try { if (con != null) con.close(); } catch (SQLException se) { /* Log or ignore */ }
        }

        // Get success or failure message from URL
        String msg = request.getParameter("msg");

        if ("success".equals(msg)) {
            out.println("<p class='success-message'>Password updated successfully.</p>");
        } else if ("failure".equals(msg)) {
            out.println("<p class='error-message'>Password update failed. Please try again.</p>");
        } else if ("error".equals(msg)) {
            out.println("<p class='error-message'>Something went wrong. Please try again.</p>");
        }

        out.println("<form action='" + contextPath + "/updatepassword' method='post' class='profile-form'>"); // Add class to form

        out.println("<div class='form-group'>"); // Group label and input
        out.println("<label for='patientName'>Full Name:</label>");
        out.println("<input type='text' id='patientName' name='patientName' value='" + fullName + "' readonly class='form-control'>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='username'>Username:</label>");
        out.println("<input type='text' id='username' name='username' value='" + username + "' readonly class='form-control'>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='age'>Age:</label>");
        out.println("<input type='text' id='age' value='" + age + "' readonly class='form-control'>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='new_password'>Change Password:</label>");
        out.println("<input type='password' id='new_password' name='new_password' placeholder='Enter new password' required class='form-control'>");
        out.println("</div>");

        out.println("<div class='form-actions'>"); // Use form-actions for button styling
        out.println("<button type='submit' class='button primary-button'>Update Password</button>");
        out.println("</div>");

        out.println("</form>");
        out.println("</div>"); // Close container

        // --- Footer (consistent theme) ---
        out.println("<div class='footer'>");
        out.println("© 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }
}