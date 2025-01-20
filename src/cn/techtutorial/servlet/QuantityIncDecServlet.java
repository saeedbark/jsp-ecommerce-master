package cn.techtutorial.servlet;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import cn.techtutorial.model.Cart;
import java.util.ArrayList;

@WebServlet("/quantity-inc-dec")
public class QuantityIncDecServlet extends HttpServlet {
    private static final long serialVersionUID = 1L;

    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String action = request.getParameter("action");
        int id = Integer.parseInt(request.getParameter("id"));
        HttpSession session = request.getSession();
        ArrayList<Cart> cart_list = (ArrayList<Cart>) session.getAttribute("cart-list");

        if (action != null && id >= 1) {
            if (action.equals("inc")) {
                for (Cart c : cart_list) {
                    if (c.getId() == id) {
                        int quantity = c.getQuantity();
                        quantity++;
                        c.setQuantity(quantity);
                        break; // Exit the loop after updating the quantity
                    }
                }
            } else if (action.equals("dec")) {
                for (Cart c : cart_list) {
                    if (c.getId() == id) {
                        int quantity = c.getQuantity();
                        if (quantity > 1) {
                            quantity--;
                            c.setQuantity(quantity);
                        }
                        break; // Exit the loop after updating the quantity
                    }
                }
            }
            response.sendRedirect("cart.jsp"); // Redirect after processing
        } else {
            response.sendRedirect("cart.jsp");
        };
    }
}