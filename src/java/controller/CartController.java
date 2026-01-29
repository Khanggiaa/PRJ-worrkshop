package controller;

import java.io.IOException;
import java.util.List;
import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import model.CartDAO;
import model.CartDTO;
import model.UserDTO;

@WebServlet(name = "CartController", urlPatterns = {"/CartController"})
public class CartController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");
        String url = "MainController?action=Search&search="; // Mặc định quay lại danh sách
        
        try {
            HttpSession session = request.getSession();
            UserDTO loginUser = (UserDTO) session.getAttribute("LOGIN_USER");

            // 1. Kiểm tra đăng nhập (Bắt buộc vì DB Cart cần userID)
            if (loginUser == null) {
                request.setAttribute("ERROR", "Vui lòng đăng nhập để thực hiện chức năng này!");
                request.getRequestDispatcher("login.jsp").forward(request, response);
                return;
            }

            String action = request.getParameter("action");
            CartDAO dao = new CartDAO();

            if ("AddToCart".equals(action)) {
                // 2. Xử lý thêm sản phẩm vào giỏ hàng trong DB
                String productID = request.getParameter("id");
                int quantity = 1; // Mặc định mỗi lần bấm là +1
                
                boolean check = dao.addToCart(loginUser.getUserID(), productID, quantity);
                if (check) {
                    request.setAttribute("MESSAGE", "Đã thêm " + productID + " vào giỏ hàng thành công!");
                } else {
                    request.setAttribute("ERROR", "Không thể thêm sản phẩm vào giỏ hàng.");
                }
                
            } else if ("ViewCart".equals(action)) {
                // ... (giữ nguyên logic cũ) ...
                List<CartDTO> cartList = dao.getCart(loginUser.getUserID());
                request.setAttribute("CART_LIST", cartList);
                url = "viewCart.jsp";
                
            } else if ("Remove".equals(action)) {
                // [NEW] Xử lý xóa sản phẩm
                String productID = request.getParameter("id");
                dao.delete(loginUser.getUserID(), productID);
                
                // Sau khi xóa, gọi lại hành động ViewCart để cập nhật danh sách
                // Dùng sendRedirect hoặc forward về chính Controller với action=ViewCart
                url = "CartController?action=ViewCart";
            }

        } catch (Exception e) {
            log("Error at CartController: " + e.toString());
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