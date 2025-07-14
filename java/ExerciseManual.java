import java.io.*;
import javax.servlet.*;
import javax.servlet.http.*;
import javax.servlet.annotation.WebServlet;

@WebServlet("/ExerciseManual")
public class ExerciseManual extends HttpServlet {
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        PrintWriter out = response.getWriter();

        String contextPath = request.getContextPath(); // Get the context path for robust linking

        // --- Start HTML Structure ---
        out.println("<!DOCTYPE html>");
        out.println("<html>");
        out.println("<head>");
        out.println("    <title>Exercise Manual</title>");
        // Link to the external CSS file using context path
        out.println("    <link rel='stylesheet' href='exercise.css'>");
        out.println("</head>");
        out.println("<body>");

        // --- Header Bar (consistent theme) ---
        out.println("<div class='header-bar'>");
        // NEW: Back button in the header
        out.println("  <a href='" + contextPath + "/PatientDashboard' class='header-back-button'>< Back to Dashboard</a>");
        out.println("  <div class='center-title'>");
        out.println("    <h1 class='welcome'>Exercise Manual</h1>");
        out.println("  </div>");
        out.println("</div>");

        // --- Main Content Container ---
        out.println("<div class='container'>");
        out.println("    <h2>Exercise Manual</h2>");

        out.println("<div class='exercise-cards-grid'>"); // Main grid for categories

        // Back Pain Category
        out.println("<div class='category card'>");
        out.println("    <img src='upload/backpain.jpg' alt='Back Pain Exercises' class='category-image'>");
        out.println("    <h3>Back Pain</h3>");
        out.println("    <div class='exercise-items-grid'>"); // Inner grid for individual exercises
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Lower Back Stretch</h4>");
        out.println("            <a href='https://youtu.be/7V-EbW-DmN0?si=p38FHI6SayC2PIZd' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Core Strengthening</h4>");
        out.println("            <a href='https://youtu.be/_lT5Cd4cDTc?si=aMDWMwI-hRPzvpZj' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Cat-Cow Pose</h4>");
        out.println("            <a href='https://youtu.be/vuyUwtHl694?si=ay0zyxI9_X2r_-KY' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Pelvic Tilts</h4>");
        out.println("            <a href='https://youtu.be/JGLQS_ArIMY?si=ZhDWjccZQk58GU51' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("    </div>"); // Close exercise-items-grid
        out.println("</div>");

        // Neck Pain Category
        out.println("<div class='category card'>");
        out.println("    <img src='upload/neck.jpg' alt='Neck Pain Exercises' class='category-image'>");
        out.println("    <h3>Neck Pain</h3>");
        out.println("    <div class='exercise-items-grid'>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Neck Mobility</h4>");
        out.println("            <a href='https://youtu.be/uN0VAViZ9nA?si=NQn93u2afq7ke29m' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Neck Stretches</h4>");
        out.println("            <a href='https://youtu.be/ekLJMzJWZU4?si=CVI7XyFtEdfzVIpd' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Chin Tucks</h4>");
        out.println("            <a href='https://youtu.be/gIBoxQ6AlS0?si=hZn0BFoleawpsxss' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Shoulder Rolls</h4>");
        out.println("            <a href='https://youtu.be/X7NtgY9kCCM?si=xxcbhewKXWulIry4' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</div>");

// Posture Correction Category
        out.println("<div class='category card'>");
        out.println("    <img src='upload/post.jpg' alt='Posture Correction Exercises' class='category-image'>");
        out.println("    <h3>Posture Correction</h3>");
        out.println("    <div class='exercise-items-grid'>");

        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Wall Angels</h4>");
        out.println("            <a href='https://youtu.be/1UU4VvklQ44?si=Xj2kgRiJ9tAOJjnM' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");

        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Chin Tucks</h4>");
        out.println("            <a href='https://youtu.be/gIBoxQ6AlS0?si=hZn0BFoleawpsxss' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");

        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4> Retraction</h4>");
        out.println("            <a href='https://youtu.be/YejnTLIA9K8?si=l23UKABxMIJDKC7c' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");

        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Thoracic</h4>");
        out.println("            <a href='https://youtu.be/csjTuWpZA10?si=bK9wU0NG-5AX3U0m' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");

        out.println("    </div>");
        out.println("</div>");


        // Knee Pain Category
        out.println("<div class='category card'>");
        out.println("    <img src='upload/kneepain.jpg' alt='Knee Pain Exercises' class='category-image'>");
        out.println("    <h3>Knee Pain</h3>");
        out.println("    <div class='exercise-items-grid'>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Quad Sets</h4>");
        out.println("            <a href='https://youtu.be/au62CidApd0?si=0qOyh0VAa0XeJRex' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Hamstring Curls</h4>");
        out.println("            <a href='https://youtu.be/M6XOjsShrJw?si=ibo1ventuLFsfbxQ' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Leg Raises</h4>");
        out.println("            <a href='https://youtu.be/Ka19yzAlIGY?si=3TJamMuBwp8yYb_j' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Calf Stretches</h4>");
        out.println("            <a href='https://youtu.be/XibsfBav_04?si=x3_hZhX0BLzQZvZw' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</div>");

        // Shoulder Pain Category
        out.println("<div class='category card'>");
        out.println("    <img src='upload/shoulder.jpg' alt='Shoulder Pain Exercises' class='category-image'>");
        out.println("    <h3>Shoulder Pain</h3>");
        out.println("    <div class='exercise-items-grid'>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Pendulum Swings</h4>");
        out.println("            <a href='https://youtu.be/OKaxBRcoxzY?si=USksSKOE3aeyCYi8' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Wall Slides</h4>");
        out.println("            <a href='https://youtube.com/shorts/T-ghuzCZqbQ?si=EOUu1pPuwhuDd1nT' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Internal Rotation</h4>");
        out.println("            <a href='https://youtu.be/VbtcHtJartc?si=cDSkP9vbNjrkwYIW' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Mobility</h4>");
        out.println("            <a href='https://youtu.be/5EybPvPoEio?si=iwXMpb1asBwjhcXo' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</div>");

        // Basic Full Body Category
        out.println("<div class='category card'>");
        out.println("    <img src='upload/fullbody.jpg' alt='Full Body Exercises' class='category-image'>");
        out.println("    <h3>Basic Full Body</h3>");
        out.println("    <div class='exercise-items-grid'>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Jumping Jacks</h4>");
        out.println("            <a href='https://youtu.be/CWpmIW6l-YA?si=ix3RtTj8HVc0QipK' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Bodyweight Squats</h4>");
        out.println("            <a href='https://youtu.be/_uZLFUnKSaM?si=5PShq1MKkywZznot' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Push-ups</h4>");
        out.println("            <a href='https://youtu.be/tWjBnQX3if0?si=pdgZXDwSFH4-7Y_g' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("        <div class='exercise-item-card'>");
        out.println("            <h4>Plank</h4>");
        out.println("            <a href='https://youtu.be/1i9D63Pa7S0?si=-7FFEAcuQe0qm6RJ' target='_blank' class='exercise-link'>Watch Video</a>");
        out.println("        </div>");
        out.println("    </div>");
        out.println("</div>");

        out.println("</div>"); // Close exercise-cards-grid

        out.println("</div>"); // Close container

        out.println("<div class='footer'>");
        out.println("© 2025 Your HealthCare System. All rights reserved.");
        out.println("</div>");

        out.println("</body></html>");
    }
}
