
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;

@WebServlet("/ViewReports")
public class ViewReports extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;

        if (username == null) {
            response.sendRedirect("patientlogin.html");  // or login.jsp
            return;
        }

        out.println("<html><head><title>My Reports</title></head><body>");
        out.println("<h2>My Uploaded Reports</h2>");

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");

            // Fetch patient name using username from reg table
            PreparedStatement psName = con.prepareStatement("SELECT patientName FROM reg WHERE username = ?");
            psName.setString(1, username);
            ResultSet rsName = psName.executeQuery();

            String patientName = null;
            if (rsName.next()) {
                patientName = rsName.getString("name");
            } else {
                out.println("<p style='color:red;'>No such user found.</p>");
                return;
            }

            // Now fetch reports ONLY for this patient name
            PreparedStatement psReports = con.prepareStatement("SELECT * FROM reports WHERE patientName = ?");
            psReports.setString(1, patientName);
            ResultSet rs = psReports.executeQuery();

            boolean hasReports = false;
            while (rs.next()) {
                hasReports = true;
                String reportDate = rs.getString("report_date");
                String description = rs.getString("description");
                String fileName = rs.getString("file_name");

                out.println("<p><b>" + patientName + "</b> | " + reportDate + " | " + description
                        + " | <a href='" + request.getContextPath() + "/uploads/" + fileName + "' target='_blank'>View PDF</a></p><hr>");
            }

            if (!hasReports) {
                out.println("<p>No reports found.</p>");
            }

            rs.close();
            psName.close();
            psReports.close();
            con.close();
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<p style='color:red;'>Error loading reports: " + e.getMessage() + "</p>");
        }

        out.println("</body></html>");
    }
}
