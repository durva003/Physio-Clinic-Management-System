import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/adminlogin")
public class adminlogin extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String username = request.getParameter("username");
        String password = request.getParameter("password");

        Connection con = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            stmt = con.createStatement();

            String query = "SELECT * FROM adminlogin WHERE username='" + username +
                           "' AND password='" + password + "'";

            ResultSet rs = stmt.executeQuery(query);

            if (rs.next()) {
                // Success: Redirect to dashboard
                HttpSession session = request.getSession();
                session.setAttribute("adminUser", username);
                response.sendRedirect("admindashboard.html");
            } else {
                // Failed: Redirect back to login with error
                response.sendRedirect("adminlogin.html?error=1");
            }

        } catch (Exception e) {
            e.printStackTrace();
            response.sendRedirect("adminlogin.html?error=1");
        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ignored) {}
            try { if (con != null) con.close(); } catch (Exception ignored) {}
        }
    }
}
