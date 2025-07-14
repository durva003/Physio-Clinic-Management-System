import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;
import java.time.LocalDate;

@WebServlet("/today")
public class today_appointments extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws IOException, ServletException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>Today's Appointments</title>");
        out.println("    <link rel='stylesheet' href='todayappointment.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div class='header-bar'>");
        out.println("        <div class='back-button-container'>");
        out.println("            <a href='admindashboard.html' class='back-button'>&#8592; Back</a>");
        out.println("        </div>");
        out.println("        <div class='center-title'>");
        out.println("            <h1 class='welcome'>Today's Appointments</h1>");
        out.println("        </div>");
        out.println("    </div>");

        out.println("    <div class='container'>");
        out.println("        <h2>Appointments for " + LocalDate.now() + "</h2>");

        String message = (String) request.getAttribute("message");
        if ("success".equals(message)) {
            out.println("<p class='success-message'>Appointment statuses updated successfully.</p>");
        } else if ("fail".equals(message)) {
            out.println("<p class='error-message'>Failed to update appointment statuses. Please try again.</p>");
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            stmt = con.createStatement();

            // ✅ JOIN appointment + status table to get actual appointment status
            String query = "SELECT a.id, a.patientName, a.appointmentDate, s.status " +
                           "FROM appointment a " +
                           "LEFT JOIN status s ON a.id = s.appointment_id " +
                           "WHERE DATE(a.appointmentDate) = CURDATE() " +
                           "ORDER BY a.appointmentDate ASC";

            rs = stmt.executeQuery(query);

            if (!rs.isBeforeFirst()) {
                out.println("<p class='no-appointments-message'>No appointments scheduled for today.</p>");
            } else {
                out.println("<form action='updateappointment' method='post'>");
                out.println("<div class='table-responsive'>");
                out.println("<table class='appointments-table'>");
                out.println("<thead><tr><th>ID</th><th>Patient</th><th>Date</th><th>Status</th><th>Prescription</th></tr></thead>");
                out.println("<tbody>");

                while (rs.next()) {
                    int id = rs.getInt("id");
                    String patient = rs.getString("patientName");
                    String date = rs.getString("appointmentDate");
                    String currentStatus = rs.getString("status");

                    if (currentStatus == null) {
                        currentStatus = "Pending";
                    }

                    out.println("<tr>");
                    out.println("<td>" + id + "<input type='hidden' name='appointment_ids' value='" + id + "'/></td>");
                    out.println("<td>" + patient + "</td>");
                    out.println("<td>" + date + "</td>");
                    out.println("<td>");
                    out.println("<select name='status_" + id + "'>");
                    out.println("<option value='Pending'" + ("Pending".equals(currentStatus) ? " selected" : "") + ">Pending</option>");
                    out.println("<option value='Done'" + ("Done".equals(currentStatus) ? " selected" : "") + ">Done</option>");
                    out.println("<option value='Cancelled'" + ("Cancelled".equals(currentStatus) ? " selected" : "") + ">Cancelled</option>");
                    out.println("</select>");
                    out.println("</td>");
                    out.println("<td><a href='prescription?appointment_id=" + id + "'>Add</a></td>");
                    out.println("</tr>");
                }

                out.println("</tbody>");
                out.println("</table>");
                out.println("</div>");
                out.println("<div class='button-group'>");
                out.println("<input type='submit' value='Update Statuses' class='button primary-button'/>");
                out.println("</div>");
                out.println("</form>");
            }

        } catch (Exception e) {
            out.println("<p class='error-message'>Error: " + e.getMessage() + "</p>");
        } finally {
            try { if (rs != null) rs.close(); } catch (Exception ex) {}
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
            try { if (con != null) con.close(); } catch (Exception ex) {}
        }

        out.println("    </div>");

        out.println("    <div class='footer'>");
        out.println("        &copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("    </div>");

        out.println("</body>");
        out.println("</html>");
        out.close();
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        doGet(request, response);
    }
}
