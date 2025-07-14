import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/adminlogout")
public class adminlogout extends HttpServlet {

    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        HttpSession session = request.getSession(false); // fetch session if exists
        if (session != null) {
            session.invalidate(); // destroy session
        }

        response.sendRedirect("adminlogin.html"); // redirect to login page
    }
}
