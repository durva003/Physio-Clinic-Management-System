import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/prescription")
public class prescription extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        // --- START: HTML Structure and CSS Link Injection ---
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Add Prescription</title>");
        // Link to the external CSS file
        out.println("    <link rel='stylesheet' href='prescription.css'>");
        out.println("</head>");
        out.println("<body>");

        // Header section
        out.println("    <div class='header-bar'>");
        out.println("        <div class='back-button-container'>");
        // Link back to the today_appointments servlet
        out.println("            <a href='today' class='back-button'>&#8592; Back</a>");
        out.println("        </div>");
        out.println("        <div class='center-title'>");
        out.println("            <h1 class='welcome'>Add Prescription</h1>");
        out.println("        </div>");
        out.println("    </div>");

        // Main content container
        out.println("    <div class='container'>");
        out.println("        <h2>Add Prescription</h2>"); // Main heading within container

        // Message display (now with CSS classes)
        String message = request.getParameter("message");
        if ("success".equals(message)) {
            out.println("<p class='success-message'>Prescription added successfully!</p>");
        } else if ("fail".equals(message)) {
            out.println("<p class='error-message'>Failed to add prescription.</p>");
        }
        // --- END: HTML Structure and CSS Link Injection ---

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            stmt = con.createStatement();
            rs = stmt.executeQuery("SELECT id FROM appointment");

            out.println("<form method='post' action='prescription' class='prescription-form'>"); // Added class to form

            // Form Groups for consistent styling
            out.println("<div class='form-group'>");
            out.println("<label for='appointment_id'>Select Appointment ID:</label>");
            out.println("<select name='appointment_id' id='appointment_id' required>"); // Added id for label for
            while (rs.next()) {
                out.println("<option value='" + rs.getInt("id") + "'>" + rs.getInt("id") + "</option>");
            }
            out.println("</select>");
            out.println("</div>"); // Close form-group

            out.println("<div class='form-group'>");
            out.println("<label for='prescription_date'>Prescription Date:</label>");
            out.println("<input type='date' name='prescription_date' id='prescription_date' required>");
            out.println("</div>"); // Close form-group

            out.println("<div class='form-group'>");
            out.println("<label for='medication'>Medication:</label>");
            out.println("<input type='text' name='medication' id='medication' required>");
            out.println("</div>"); // Close form-group

            out.println("<div class='form-group'>");
            out.println("<label for='exercises'>Exercises:</label>");
            out.println("<textarea name='exercises' id='exercises' required></textarea>");
            out.println("</div>"); // Close form-group

            out.println("<div class='form-group'>");
            out.println("<label for='notes'>Notes:</label>");
            out.println("<textarea name='notes' id='notes' required></textarea>");
            out.println("</div>"); // Close form-group

            out.println("<div class='button-group'>"); // Added button group for centering
            out.println("<input type='submit' value='Add Prescription' class='button primary-button'>"); // Added CSS classes
            out.println("</div>"); // Close button-group
            out.println("</form>");

        } catch (Exception e) {
            out.println("<p class='error-message'>Error: " + e.getMessage() + "</p>"); // Added CSS class
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) { ex.printStackTrace(); } // Added printStackTrace for debugging
            try { if (stmt != null) stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception ex) { ex.printStackTrace(); }
        }

  
        out.println("    </div>"); // Close .container

        // Footer section
        out.println("    <div class='footer'>");
        out.println("        &copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("    </div>");

        out.println("</body>");
        out.println("</html>");
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws IOException {
        String appointmentId = request.getParameter("appointment_id");
        String prescriptionDate = request.getParameter("prescription_date");
        String medication = request.getParameter("medication");
        String exercises = request.getParameter("exercises");
        String notes = request.getParameter("notes");

        Connection con = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            stmt = con.createStatement();

            // Original query, unchanged
            String query = "INSERT INTO appointment_updates (appointment_id, prescription_date, medication,exercises, notes) VALUES ('"
                            + appointmentId + "', '" + prescriptionDate + "', '" + medication + "', '"+ exercises +"','" + notes + "')";

            int result = stmt.executeUpdate(query);

            if (result > 0) {
                response.sendRedirect("prescription?message=success");
            } else {
                response.sendRedirect("prescription?message=fail");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("prescription?message=fail");
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) { ex.printStackTrace(); }
            try { if (con != null) con.close(); } catch (Exception ex) { ex.printStackTrace(); }
        }
    }
}