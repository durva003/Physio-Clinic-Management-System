import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/PatientDashboard")
public class PatientDashboard extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
        throws ServletException, IOException {
            
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String fullName = (session != null) ? (String) session.getAttribute("patientName") : null;

        String contextPath = request.getContextPath();

        if (fullName == null) {
            response.sendRedirect(contextPath + "/patientlogin.html");
            return;
        }

        out.println("<!DOCTYPE html>");
        out.println("<html><head><title>Patient Dashboard</title>");
        out.println("<link rel='stylesheet' href='dashboard.css'>");
        out.println("</head><body>");

        // --- START: Updated Header Bar Structure ---
        out.println("<div class='header-bar'>");
        out.println("  <div class='left-content'></div>"); // Still placeholder for left side
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Welcome, " + fullName + "</h1>");
        out.println("  </div>");
        // NEW: Separate div for profile icon and logout link
        out.println("  <div class='header-right-group'>");
        out.println("    <a href='" + contextPath + "/PatientProfile' class='profile-icon-link'><img src='upload/icon.png' alt='Profile'></a>");
        out.println("    <a href='" + contextPath + "/logout' class='logout-link'>Logout</a>");
        out.println("  </div>");
        out.println("</div>");
        // --- END: Updated Header Bar Structure ---

        out.println("<div class='dashboard'>");
        out.println("<div class='card'><h2>Book Appointment</h2><a href='" + contextPath + "/BookAppointment'>Go</a></div>");
        out.println("<div class='card'><h2>View Prescriptions</h2><a href='" + contextPath + "/viewprescriptions'>Go</a></div>");
        out.println("<div class='card'><h2>Upload Reports</h2><a href='" + contextPath + "/report.html'>Go</a></div>");
        out.println("<div class='card'><h2>Exercises </h2><a href='" + contextPath + "/ExerciseManual'>Go</a></div>");
        out.println("<div class='card'><h2>Appointment History</h2><a href='" + contextPath + "/AppointmentHistory'>Go</a></div>");
        out.println("</div>");

        out.println("<div class='footer'>");
        out.println("&copy; 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }
}