package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@WebServlet(name = "MainController", urlPatterns = {"/MainController"})
public class MainController extends HttpServlet {

    private static final String ERROR = "error.jsp"; //
    private static final String LOGIN = "LoginController"; //
    private static final String CREATE = "CreateController"; //
    private static final String SEARCH = "SearchController"; //
    private static final String LOGOUT = "LogoutController"; //
    private static final String ADD_TO_CART = "CartController"; //
    private static final String VIEW_CART = "CartController"; //
    private static final String DELETE = "DeleteController"; //
    private static final String UPDATE = "UpdateController"; //
    private static final String VIEW_UPDATE = "ViewUpdateController"; //

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = ERROR;
        try {
            String action = request.getParameter("action");

            if (action == null || action.isEmpty()) {
                url = SEARCH; // Mặc định vào trang tìm kiếm
            } else if ("Login".equals(action)) {
                url = LOGIN;
            } else if ("Logout".equals(action)) {
                url = LOGOUT;
            } else if ("Create".equals(action)) {
                url = CREATE;
            } else if ("Search".equals(action)) {
                url = SEARCH;
            } else if ("AddToCart".equals(action)) {
                url = ADD_TO_CART;
            } else if ("ViewCart".equals(action)) {
                url = VIEW_CART;
            } else if ("Delete".equals(action)) {
                url = DELETE;
            } else if ("Update".equals(action)) {
                url = UPDATE;
            } else if ("ViewUpdate".equals(action)) {
                url = VIEW_UPDATE;
            }
        } catch (Exception e) {
            log("Error at MainController: " + e.toString());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        processRequest(request, response);
    }
}