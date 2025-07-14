import java.io.IOException;
import java.io.PrintWriter;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/reg")
public class reg extends HttpServlet {

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        // Read form fields
        String patientName = request.getParameter("patientName");
        String contact = request.getParameter("contact");
        String address = request.getParameter("address");
        String dob = request.getParameter("dob");
        String gender = request.getParameter("gender");
        String ageStr = request.getParameter("age");
        String email = request.getParameter("email");
        String username = request.getParameter("username");
        String password = request.getParameter("password");
        String confirmPassword = request.getParameter("confirm_password");

        try {
            int age = Integer.parseInt(ageStr);

            // Check if passwords match
            if (!password.equals(confirmPassword)) {
                out.println("<h3 style='color:red;'>Passwords do not match!</h3>");
                return;
            }

            // Load JDBC Driver
            Class.forName("com.mysql.cj.jdbc.Driver");

            // Connect to MySQL
            Connection con = DriverManager.getConnection(
                    "jdbc:mysql://localhost:3306/physio", "root", "");

            Statement stmt = con.createStatement();

            // Insert into database
            String query = "INSERT INTO reg (patientName, contact, address, dob, gender, age, email, username, password) VALUES ('"
                    + patientName + "', '" + contact + "', '" + address + "', '" + dob + "', '"
                    + gender + "', " + age + ", '" + email + "', '" + username + "', '" + password + "')";

            int result = stmt.executeUpdate(query);

            if (result > 0) {
                response.sendRedirect("patientlogin.html"); 
            } else {
                out.println("<h3 style='color:red;'>Registration Failed.</h3>");
            }


            stmt.close();
            con.close();

        } catch (NumberFormatException e) {
            out.println("<h3 style='color:red;'>Invalid age value!</h3>");
        } catch (SQLIntegrityConstraintViolationException e) {
            out.println("<h3 style='color:red;'>Username already exists!</h3>");
        } catch (Exception e) {
            e.printStackTrace();
            out.println("<h3 style='color:red;'>Error: " + e.getMessage() + "</h3>");
        }
    }
}
