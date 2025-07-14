import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/viewpatients")
public class viewpatients extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws IOException {
        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        // Start HTML output
        out.println("<!DOCTYPE html>");
        out.println("<html lang='en'>");
        out.println("<head>");
        out.println("    <meta charset='UTF-8'>");
        out.println("    <title>View Patients</title>");
        // Corrected Link to the external CSS file: added 'css/' directory
        out.println("    <link rel='stylesheet' href='patientlist.css'>");
        out.println("</head>");
        out.println("<body>");

        out.println("    <div class='header-bar'>");
        out.println("        <div class='back-button-container'>");
        out.println("            <a href='admindashboard.html' class='back-button'>&#8592; Back</a>");
        out.println("        </div>");
        out.println("        <div class='center-title'>");
        out.println("            <h1 class='welcome'>View Patients</h1>");
        out.println("        </div>");
        out.println("    </div>");

        // Main content container (consistent white box style)
        out.println("    <div class='container'>");
        out.println("        <h2>Registered Patients</h2>");
        out.println("        <ul class='patient-list'>"); // Apply list styling via this class

        // Database interaction to fetch patient data
        try (Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
             Statement stmt = con.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT patientName, username FROM reg")) {

            // Check if the ResultSet is empty before iterating
            if (!rs.isBeforeFirst()) {
                out.println("<p class='no-patients-message'>No patients registered yet.</p>");
            } else {
                // Loop through results and generate list items
                while (rs.next()) {
                    String name = rs.getString("patientName");
                    String username = rs.getString("username");
                    // Link to PatientDetails servlet, passing username as parameter
                    out.println("<li><a href='PatientDetails?username=" + username + "'>" + name + "</a></li>");
                }
            }

        } catch (SQLException e) {
            // Handle SQL exceptions gracefully
            out.println("<p class='error-message'>Error fetching patient data: " + e.getMessage() + "</p>");
            // It's good practice to log the full stack trace on the server-side for debugging
            e.printStackTrace();
        } catch (Exception e) {
            // Catch any other unexpected exceptions
            out.println("<p class='error-message'>An unexpected error occurred: " + e.getMessage() + "</p>");
            e.printStackTrace();
        }

        out.println("        </ul>");
        out.println("    </div>"); // Close .container

        // Footer section (consistent with other styled pages)
        out.println("    <div class='footer'>");
        out.println("        &copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("    </div>");

        // End HTML output
        out.println("</body>");
        out.println("</html>");

        out.close(); // Close the PrintWriter
    }
}