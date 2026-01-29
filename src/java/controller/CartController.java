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

                // Lấy số lượng từ tham số request, mặc định là 1 nếu lỗi hoặc không có
                int quantity = 1;
                try {
                    String quantityRaw = request.getParameter("quantity");
                    if (quantityRaw != null && !quantityRaw.isEmpty()) {
                        quantity = Integer.parseInt(quantityRaw);
                        // Đảm bảo số lượng luôn dương
                        if (quantity < 1) {
                            quantity = 1;
                        }
                    }
                } catch (NumberFormatException e) {
                    log("Invalid quantity format: " + e.toString());
                    quantity = 1; // Fallback về 1 nếu parse lỗi
                }

                // Gọi DAO để thêm vào DB
                boolean check = dao.addToCart(loginUser.getUserID(), productID, quantity);

                if (check) {
                    request.setAttribute("MESSAGE", "Đã thêm " + quantity + " sản phẩm " + productID + " vào giỏ hàng thành công!");
                } else {
                    request.setAttribute("ERROR", "Không thể thêm sản phẩm vào giỏ hàng.");
                }
            } else if ("ViewCart".equals(action)) {
                // 3. Xử lý lấy danh sách giỏ hàng từ DB để hiển thị
                List<CartDTO> cartList = dao.getCart(loginUser.getUserID());
                request.setAttribute("CART_LIST", cartList);
                url = "viewCart.jsp";
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
