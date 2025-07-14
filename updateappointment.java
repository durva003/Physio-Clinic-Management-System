import java.io.*;
import javax.servlet.*;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.*;
import java.sql.*;

@WebServlet("/updateappointment")
public class updateappointment extends HttpServlet {

    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String[] appointmentIds = request.getParameterValues("appointment_ids");

        Connection con = null;
        Statement stmt = null;

        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://localhost:3306/physio", "root", "");
            stmt = con.createStatement();

            for (String idStr : appointmentIds) {
                int id = Integer.parseInt(idStr);
                String newStatus = request.getParameter("status_" + id);

                // Try to update
                String updateQuery = "UPDATE status SET status = '" + newStatus + "', updated_on = NOW() " +
                                     "WHERE appointment_id = " + id;
                int rows = stmt.executeUpdate(updateQuery);

                // If not updated, insert new
                if (rows == 0) {
                    String insertQuery = "INSERT INTO status (appointment_id, status, updated_on) " +
                                         "VALUES (" + id + ", '" + newStatus + "', NOW())";
                    stmt.executeUpdate(insertQuery);
                    System.out.println("Inserted new status for appointment_id = " + id);
                } else {
                    System.out.println("Updated status for appointment_id = " + id);
                }
            }

            request.setAttribute("message", "success");

        } catch (Exception e) {
            e.printStackTrace();
            request.setAttribute("message", "fail");

        } finally {
            try { if (stmt != null) stmt.close(); } catch (Exception ex) {}
            try { if (con != null) con.close(); } catch (Exception ex) {}
        }

        // Forward back to /today servlet
        RequestDispatcher rd = request.getRequestDispatcher("/today");
        rd.forward(request, response);
    }
}
