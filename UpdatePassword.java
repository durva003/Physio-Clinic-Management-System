
import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/updatepassword")
public class UpdatePassword extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false);
        String username = (session != null) ? (String) session.getAttribute("username") : null;
        String newPassword = request.getParameter("new_password");

        if (username == null || newPassword == null || newPassword.isEmpty()) {
            response.sendRedirect("patientlogin.html");
            return;
        }

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            Statement stmt = con.createStatement();
            String query = "UPDATE reg SET password='" + newPassword + "' WHERE username='" + username + "'";
            int rowsUpdated = stmt.executeUpdate(query);

            stmt.close();
            con.close();

            if (rowsUpdated > 0) {
                response.sendRedirect("PatientProfile?msg=success");
            } else {
                response.sendRedirect("PatientProfile?msg=failure");
            }

        } catch (Exception e) {
            response.sendRedirect("PatientProfile?msg=error");
        }
    }
}
