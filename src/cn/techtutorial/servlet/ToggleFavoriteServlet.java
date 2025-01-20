package cn.techtutorial.servlet;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

@WebServlet("/toggle-favorite")
public class ToggleFavoriteServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        int productId = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();

        // Get the list of favorite product IDs from the session
        List<Integer> favorites = (List<Integer>) session.getAttribute("favorites");
        if (favorites == null) {
            favorites = new ArrayList<>();
        }

        // Toggle the favorite status
        if (favorites.contains(productId)) {
            favorites.remove(Integer.valueOf(productId)); // Remove from favorites
        } else {
            favorites.add(productId); // Add to favorites
        }

        // Update the session with the modified favorites list
        session.setAttribute("favorites", favorites);

        // Redirect back to the previous page
        response.sendRedirect(request.getHeader("referer"));
    }
}