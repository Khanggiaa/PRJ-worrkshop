<%@page import="model.ProductDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Update Product</title>
    </head>
    <body>
        <h1>Update Product</h1>
        
        <%-- BỔ SUNG: Phần hiển thị thông báo lỗi --%>
        <%
            String error = (String) request.getAttribute("ERROR");
            if (error != null) {
        %>
            <h3 style="color: red; background-color: #ffcccc; padding: 10px; border: 1px solid red;">
                <%= error %>
            </h3>
        <%
            }
        %>
        <%-- KẾT THÚC BỔ SUNG --%>

        <%
            ProductDTO p = (ProductDTO) request.getAttribute("PRODUCT");
            if (p != null) {
        %>
        <form action="MainController" method="POST">
            ID: <%= p.getId() %> <input type="hidden" name="id" value="<%= p.getId() %>"/><br/>
            
            <%-- Giữ lại giá trị người dùng nhập (sticky form) --%>
            Name: <input type="text" name="name" value="<%= p.getName() %>" required/><br/>
            Category: <input type="text" name="category" value="<%= p.getCategory() %>" required/><br/>
            Price: <input type="number" step="any" name="price" value="<%= p.getPrice() %>" required/><br/>
            Stock Quantity: <input type="number" name="stockQuantity" value="<%= p.getStockQuantity() %>" required/><br/>
            
            <%-- Xử lý null cho lastSearch để tránh hiện chữ "null" --%>
            <%
                String lastSearch = request.getParameter("lastSearch");
                if (lastSearch == null) lastSearch = "";
            %>
            <input type="hidden" name="lastSearch" value="<%= lastSearch %>"/>
            
            <input type="submit" name="action" value="Update"/>
        </form>
        <%
            }
        %>
    </body>
</html>