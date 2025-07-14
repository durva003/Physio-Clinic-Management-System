import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;
import java.sql.*;

@WebServlet("/patientlogin")
public class patientlogin extends HttpServlet {
    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        String enteredUsername = request.getParameter("username");
        String enteredPassword = request.getParameter("password");

        response.setContentType("text/html");
        PrintWriter out = response.getWriter();

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");

            Statement stmt = conn.createStatement();
            String sql = "SELECT * FROM reg WHERE username = '" + enteredUsername + "' AND password = '" + enteredPassword + "'";

            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
     
                String patientName = rs.getString("patientName");

            
                HttpSession session = request.getSession();
                session.setAttribute("patientName", patientName);
                session.setAttribute("username", enteredUsername);  // <-- Added this line

               
                response.sendRedirect("PatientDashboard");
            } else {
                
                response.sendRedirect("patientlogin.html?error=invalid");
            }

            rs.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            out.println("<html><body>");
            out.println("<p style='color:red;'>Error: " + e.getMessage() + "</p>");
            out.println("</body></html>");
        }
    }
}
