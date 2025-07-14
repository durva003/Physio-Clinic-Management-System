import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/BookAppointment")
public class BookAppointment extends HttpServlet {

    private static final String DB_URL = "jdbc:mysql://localhost:3306/physio";
    private static final String DB_USER = "root";
    private static final String DB_PASS = ""; // Consider using environment variables or a secure configuration for passwords

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String patientName = (session != null) ? (String) session.getAttribute("patientName") : "";

        String error = request.getParameter("error");
        String success = request.getParameter("success");
        String errorMessage = request.getParameter("msg"); // Retrieve the specific error message

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Book Appointment</title>");
        out.println("<link rel='stylesheet' href='appointment.css'>"); // Link to external CSS
        out.println("<script>");
        out.println("let selectedButton = null;");
        out.println("function selectSlot(button, time) {");
        out.println(" document.getElementById('appointmentTime').value = time;");
        out.println(" if (selectedButton) selectedButton.classList.remove('selected');");
        out.println(" button.classList.add('selected');");
        out.println(" selectedButton = button;");
        out.println("}");
        out.println("function validateForm() {");
        out.println(" const time = document.getElementById('appointmentTime').value;");
        out.println(" if (!time) { alert('Please select a time slot before booking.'); return false; }");
        out.println(" return true;");
        out.println("}");
        out.println("window.onload = function() {");
        if ("1".equals(error)) {
            // Set the specific error message, defaulting if none provided
            out.println("document.getElementById('errorMsg').innerText = '" + (errorMessage != null ? escapeJavaScript(errorMessage) : "An error occurred during booking. Please try again.") + "';");
            out.println("document.getElementById('errorMsg').style.display = 'block';");
            out.println("setTimeout(() => document.getElementById('errorMsg').style.display = 'none', 4000);");
        }
        if ("1".equals(success)) {
            out.println("document.getElementById('successMsg').style.display = 'block';");
            out.println("setTimeout(() => document.getElementById('successMsg').style.display = 'none', 4000);");
        }
        // Set min date to today for appointmentDate input
        out.println("const today = new Date();");
        out.println("const yyyy = today.getFullYear();");
        out.println("const mm = String(today.getMonth() + 1).padStart(2, '0');"); // Months start at 0!
        out.println("const dd = String(today.getDate()).padStart(2, '0');");
        out.println("const minDate = yyyy + '-' + mm + '-' + dd;");
        out.println("document.getElementById('appointmentDate').setAttribute('min', minDate);");

        out.println("};"); // Close window.onload
        out.println("</script>");
        out.println("</head><body>");

        // Header Bar (consistent with dashboard) - Corrected HTML structure
        out.println("<div class='header-bar'>"); // START of header-bar div
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Book Your Appointment</h1>");
        out.println("  </div>"); // END of center-title div
        out.println("</div>"); // CORRECTED: END of header-bar div

        out.println("<div class='container'>");
        // Updated error message div to be empty initially and filled by JS
        out.println("<div id='errorMsg' class='message error-message' style='display:none;'></div>");
        out.println("<div id='successMsg' class='message success-message' style='display:none;'>Appointment booked successfully!</div>");

        out.println("<form action='BookAppointment' method='post' onsubmit='return validateForm()'>");

        out.println("<div class='form-group'>");
        out.println("<label for='patientName'>Patient Name:</label>");
        // Security: Ensure patientName doesn't contain HTML special characters if it came from user input previously
        out.println("<input type='text' id='patientName' name='patientName' readonly value='" + escapeHtml(patientName) + "'>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label for='appointmentDate'>Appointment Date:</label>");
        out.println("<input type='date' id='appointmentDate' name='appointmentDate' required>");
        out.println("</div>");

        out.println("<div class='form-group'>");
        out.println("<label>Select Time Slot:</label>");
        out.println("<div class='time-slots'>"); // Container for time slots
        out.println("<p><b>Morning:</b></p>");
        out.println("<div class='slot-row'>"); // Row for morning slots
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '08:00')\">08:00 AM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '09:00')\">09:00 AM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '10:00')\">10:00 AM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '11:00')\">11:00 AM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '12:00')\">12:00 PM</button>");
        out.println("</div>"); // End slot-row

        out.println("<p><b>Evening:</b></p>");
        out.println("<div class='slot-row'>"); // Row for evening slots
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '17:00')\">05:00 PM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '18:00')\">06:00 PM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '19:00')\">07:00 PM</button>");
        out.println("<button type='button' class='slot' onclick=\"selectSlot(this, '20:00')\">08:00 PM</button>");
        out.println("</div>"); // End slot-row
        out.println("</div>"); // End time-slots
        out.println("</div>"); // End form-group for time slots

        out.println("<input type='hidden' id='appointmentTime' name='appointmentTime' required>");

        out.println("<div class='form-group'>");
        out.println("<label for='reason'>Reason for Visit:</label>");
        out.println("<textarea id='reason' name='reason' rows='4' required></textarea>");
        out.println("</div>");

        out.println("<div class='form-actions'>");
        out.println("<input type='submit' value='Book Appointment' class='button primary-button'>");
        out.println("<button type='button' onclick=\"window.location.href='PatientDashboard'\" class='button secondary-button'>Back to Dashboard</button>");
        out.println("</div>");

        out.println("</form>");
        out.println("</div>"); // End container

        out.println("<div class='footer'>");
        out.println("&copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        HttpSession session = request.getSession(false);
        String patientName = (session != null) ? (String) session.getAttribute("patientName") : null;

        if (patientName == null) {
            // Redirect to patient login if session is invalid or patientName is not set
            response.sendRedirect("patientlogin.html");
            return;
        }

        String appointmentDate = request.getParameter("appointmentDate");
        String appointmentTime = request.getParameter("appointmentTime");
        String reason = request.getParameter("reason");

        // Basic validation for required fields
        if (appointmentDate == null || appointmentDate.isEmpty() ||
            appointmentTime == null || appointmentTime.isEmpty() ||
            reason == null || reason.isEmpty()) {
            response.sendRedirect("BookAppointment?error=1&msg=All fields are required. Please fill them.");
            return;
        }

        Connection con = null;
        PreparedStatement psCount = null;
        PreparedStatement psCheckExisting = null;
        PreparedStatement psInsert = null;
        ResultSet rs = null;
        ResultSet existingRs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection(DB_URL, DB_USER, DB_PASS);

            // 1. Check if slot is full (allowing up to 2 appointments per slot)
            String countQuery = "SELECT COUNT(*) FROM appointment WHERE appointmentDate = ? AND appointmentTime = ?";
            psCount = con.prepareStatement(countQuery);
            psCount.setString(1, appointmentDate);
            psCount.setString(2, appointmentTime);
            rs = psCount.executeQuery();

            int count = 0;
            if (rs.next()) {
                count = rs.getInt(1);
            }

            if (count >= 2) { // Logic for slot full (can be adjusted)
                response.sendRedirect("BookAppointment?error=1&msg=" + escapeUrl("Appointment slot is full. Please choose another."));
            } else {
                // 2. Check if the patient already has an appointment for this specific date and time
                String checkExistingQuery = "SELECT COUNT(*) FROM appointment WHERE patientName = ? AND appointmentDate = ? AND appointmentTime = ?";
                psCheckExisting = con.prepareStatement(checkExistingQuery);
                psCheckExisting.setString(1, patientName);
                psCheckExisting.setString(2, appointmentDate);
                psCheckExisting.setString(3, appointmentTime);
                existingRs = psCheckExisting.executeQuery();

                int existingAppointments = 0;
                if (existingRs.next()) {
                    existingAppointments = existingRs.getInt(1);
                }

                if (existingAppointments > 0) {
                    response.sendRedirect("BookAppointment?error=1&msg=" + escapeUrl("You already have an appointment at this date and time. Please choose another slot or view your existing appointments."));
                } else {
                    // 3. Insert new appointment
                    String insertQuery = "INSERT INTO appointment (patientName, appointmentDate, appointmentTime, reason) VALUES (?, ?, ?, ?)";
                    psInsert = con.prepareStatement(insertQuery);
                    psInsert.setString(1, patientName);
                    psInsert.setString(2, appointmentDate);
                    psInsert.setString(3, appointmentTime);
                    psInsert.setString(4, reason);
                    psInsert.executeUpdate();
                    response.sendRedirect("BookAppointment?success=1");
                }
            }
        } catch (SQLException se) {
            se.printStackTrace();
            // In a real application, you might log the full exception and give a more user-friendly message
            response.sendRedirect("BookAppointment?error=1&msg=" + escapeUrl("Database error occurred while booking. Please try again."));
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
            response.sendRedirect("BookAppointment?error=1&msg=" + escapeUrl("Server configuration error. Database driver not found."));
        } finally {
            // Close resources in reverse order of opening
            try { if (rs != null) rs.close(); } catch (SQLException se) { se.printStackTrace(); }
            try { if (existingRs != null) existingRs.close(); } catch (SQLException se) { se.printStackTrace(); }
            try { if (psCount != null) psCount.close(); } catch (SQLException se) { se.printStackTrace(); }
            try { if (psCheckExisting != null) psCheckExisting.close(); } catch (SQLException se) { se.printStackTrace(); }
            try { if (psInsert != null) psInsert.close(); } catch (SQLException se) { se.printStackTrace(); }
            try { if (con != null) con.close(); } catch (SQLException se) { se.printStackTrace(); }
        }
    }

    // Helper method to escape JavaScript strings (for messages passed to JS)
    private String escapeJavaScript(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("\\", "\\\\")
                   .replace("'", "\\'")
                   .replace("\"", "\\\"")
                   .replace("\n", "\\n")
                   .replace("\r", "\\r");
    }

    // Helper method to HTML-escape strings (for values printed directly into HTML attributes/content)
    private String escapeHtml(String text) {
        if (text == null) {
            return "";
        }
        return text.replace("&", "&amp;")
                   .replace("<", "&lt;")
                   .replace(">", "&gt;")
                   .replace("\"", "&quot;")
                   .replace("'", "&#x27;");
    }

    // Helper method to URL-encode strings (for messages passed in URL parameters)
    private String escapeUrl(String text) {
        if (text == null) {
            return "";
        }
        try {
            return java.net.URLEncoder.encode(text, "UTF-8");
        } catch (java.io.UnsupportedEncodingException e) {
            // This should virtually never happen for UTF-8
            return text; // Fallback if encoding fails
        }
    }
}