package controller;

import java.io.IOException;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import model.ProductDAO;
import model.ProductDTO;

public class CreateController extends HttpServlet {

    protected void processRequest(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        response.setContentType("text/html;charset=UTF-8");

        String url = "createProduct.jsp";

        try {
            String id = ProductDAO.getNextID();
            String name = request.getParameter("name");
            String category = request.getParameter("category");
            String priceStr = request.getParameter("price");
            String stockStr = request.getParameter("stockQuantity");
            String lastSearch = request.getParameter("lastSearch");

            if (lastSearch == null) {
                lastSearch = "";
            }

            // --- BẮT ĐẦU SỬA ĐỔI ---
            // 1. Validate dữ liệu trước khi tạo DTO
            float price = 0;
            int stockQuantity = 0;
            boolean validationError = false;

            try {
                price = Float.parseFloat(priceStr);
                stockQuantity = Integer.parseInt(stockStr);

                // Validate miền giá trị (Optional nhưng nên có)
                if (price < 0 || stockQuantity < 0) {
                    throw new NumberFormatException("Negative value");
                }

            } catch (NumberFormatException e) {
                validationError = true;
                request.setAttribute("ERROR", "Invalid Price or Stock Quantity! Must be positive numbers.");
            }

            // 2. Chỉ thực hiện Create nếu dữ liệu hợp lệ
            ProductDTO product = new ProductDTO(id, name, category, price, stockQuantity);

            if (validationError) {
                // Nếu lỗi format số: Quay lại trang create ngay lập tức
                url = "createProduct.jsp";
                request.setAttribute("PRODUCT", product); // Giữ lại thông tin đã nhập
            } else {
                // Nếu dữ liệu sạch: Gọi DAO
                ProductDAO dao = new ProductDAO();
                boolean check = dao.create(product);

                if (check) {
                    url = "MainController?action=Search&search=" + lastSearch;
                } else {
                    request.setAttribute("ERROR", "Update failed (Database Error)");
                    request.setAttribute("PRODUCT", product);
                    url = "createProduct.jsp";
                }
            }
            // --- KẾT THÚC SỬA ĐỔI ---

        } catch (Exception e) {
            log("Error at CreateController: " + e.toString());
            // QUAN TRỌNG: Gửi thông báo lỗi về cho người dùng
            request.setAttribute("ERROR", "Hệ thống gặp lỗi: " + e.getMessage());
        } finally {
            request.getRequestDispatcher(url).forward(request, response);
        }
    }
}
