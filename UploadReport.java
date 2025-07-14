
import java.io.*;
import java.sql.*;
import javax.servlet.*;
import javax.servlet.annotation.*;
import javax.servlet.http.*;

@WebServlet("/UploadReport")
@MultipartConfig
public class UploadReport extends HttpServlet {

    private static final String UPLOAD_DIR = "uploads";

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession();
        String username = (String) session.getAttribute("username");
        String patientName = "";

        String reportDate = request.getParameter("report_date");
        String description = request.getParameter("description");
        Part filePart = request.getPart("report");

        String fileName = new File(filePart.getSubmittedFileName()).getName();
        String uploadPath = getServletContext().getRealPath("") + File.separator + UPLOAD_DIR;

        File uploadDir = new File(uploadPath);
        if (!uploadDir.exists()) {
            uploadDir.mkdir();
        }

        String filePath = uploadPath + File.separator + fileName;
        filePart.write(filePath);

        Connection con = null;
        Statement stmt = null;
        ResultSet rs = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            stmt = con.createStatement();

            // Fetch full name from reg table using session username
            rs = stmt.executeQuery("SELECT patientName FROM reg WHERE username = '" + username + "'");
            if (rs.next()) {
                patientName = rs.getString("patientName");
            }

            // Insert report info into database
            String query = "INSERT INTO reports (patient_name, report_date, description, file_name, file_path) "
                    + "VALUES ('" + patientName + "', '" + reportDate + "', '" + description + "', '" + fileName + "', '" + filePath + "')";
            stmt.executeUpdate(query);

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (rs != null) {
                    rs.close();
                }
            } catch (Exception e) {
            }
            try {
                if (stmt != null) {
                    stmt.close();
                }
            } catch (Exception e) {
            }
            try {
                if (con != null) {
                    con.close();
                }
            } catch (Exception e) {
            }
        }

        response.sendRedirect("report.html?success=true");
    }
}
