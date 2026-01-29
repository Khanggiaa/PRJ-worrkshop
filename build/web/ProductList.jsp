<%@page import="java.util.List"%>
<%@page import="model.ProductDTO"%>
<%@page import="model.UserDTO"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Product List</title>
        <style>
            table, th, td {
                border: 1px solid black;
                border-collapse: collapse;
                padding: 5px;
            }
            .msg {
                color: green;
            }
            .err {
                color: red;
            }
        </style>
    </head>
    <body>
        <%
            // Lấy user từ session (có thể null nếu chưa login)
            UserDTO user = (UserDTO) session.getAttribute("LOGIN_USER");

            // Xác định quyền
            boolean isLoggedIn = (user != null);
            boolean isAdmin = (isLoggedIn && user.getRoleID() == 1); // 1 = Admin
            boolean isUser = (isLoggedIn && user.getRoleID() == 2);  // 2 = User
        %>

        <%-- Header: Chào mừng hoặc Link Login --%>
        <% if (isLoggedIn) {%>
        <h1>Welcome: <%= user.getFullName()%> (Role: <%= isAdmin ? "Admin" : "User"%>)</h1>
        <form action="MainController" method="POST">
            <input type="submit" name="action" value="Logout"/>
        </form>

        <%-- User mới hiện Cart --%>
        <% if (isUser) { %>
        <h3><a href="CartController?action=ViewCart">View Cart</a></h3>
        <% } %>

        <% } else { %>
        <h1>Welcome Guest! <a href="login.jsp">Login</a> to shop or manage.</h1>
        <% }%>

        <br/>

        <%-- Search (Ai cũng dùng được) --%>
        <form action="MainController" method="POST">
            Search Product: 
            <input type="text" name="search" value="<%= request.getParameter("search") == null ? "" : request.getParameter("search")%>"/>
            <input type="submit" name="action" value="Search"/>
        </form>

        <%-- Thông báo lỗi/thành công --%>
        <h3 class="err"><%= request.getAttribute("ERROR") != null ? request.getAttribute("ERROR") : ""%></h3>
        <h3 class="msg"><%= request.getAttribute("MESSAGE") != null ? request.getAttribute("MESSAGE") : ""%></h3>

        <%-- Admin mới thấy nút Add New Product --%>
        <% if (isAdmin) { %>
        <br/><a href="createProduct.jsp">Create New Product</a><br/>
        <% } %>

        <%-- Display List --%>
        <%
            List<ProductDTO> list = (List<ProductDTO>) request.getAttribute("LIST_PRODUCT");
            if (list != null && !list.isEmpty()) {
        %>
        <br/>
        <table>
            <thead>
                <tr>
                    <th>No</th>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Category</th>
                    <th>Price</th>
                    <th>Stock</th>
                    <th>Action</th> <%-- Cột Action thay đổi theo role --%>
                </tr>
            </thead>
            <tbody>
                <%
                    int count = 1;
                    for (ProductDTO p : list) {
                %>
                <tr>
                    <%-- Nếu là Admin thì dùng Form để update/delete, Guest/User chỉ xem text --%>
                    <% if (isAdmin) {%>
            <form action="MainController" method="POST">
                <td><%= count++%></td>
                <td><%= p.getId()%><input type="hidden" name="id" value="<%= p.getId()%>"/></td>
                <td><input type="text" name="name" value="<%= p.getName()%>" required/></td>
                <td><input type="text" name="category" value="<%= p.getCategory()%>" required/></td>
                <td><input type="number" step="any" name="price" value="<%= p.getPrice()%>" required/></td>
                <td><input type="number" name="stockQuantity" value="<%= p.getStockQuantity()%>" required/></td>
                <td>
                    <input type="hidden" name="search" value="<%= request.getParameter("search")%>"/>
                    <input type="submit" name="action" value="Update"/>
                    <input type="submit" name="action" value="Delete" onclick="return confirm('Delete <%= p.getName()%>?');"/>
                </td>
            </form>
            <% } else {%>
            <%-- Giao diện cho User và Guest (Chỉ xem, không sửa) --%>
            <td><%= count++%></td>
            <td><%= p.getId()%></td>
            <td><%= p.getName()%></td>
            <td><%= p.getCategory()%></td>
            <td><%= p.getPrice()%></td>
            <td><%= p.getStockQuantity()%></td>
            <td>
                <%-- User thấy nút Add to Cart --%>
                <% if (isUser) {%>
                <form action="MainController" method="POST">
                    <input type="hidden" name="id" value="<%= p.getId()%>"/>
                    <input type="hidden" name="search" value="<%= request.getParameter("search") == null ? "" : request.getParameter("search")%>"/>
                    <label>Qty:</label>
                    <input type="number" name="quantity" value="1" min="1" max="<%= p.getStockQuantity()%>" required style="width: 50px; text-align: center;"/>
                    <input type="submit" name="action" value="AddToCart"/>
                </form>
                <% } else if (!isLoggedIn) {%>
                <%-- Guest bấm AddToCart sẽ bị controller đẩy về Login --%>
                <a href="MainController?action=AddToCart&id=<%= p.getId()%>">Add to Cart</a>
                <% } %>
            </td>
            <% } %>
        </tr>
        <%
            }
        %>
    </tbody>
</table>
<%
    }
%>
</body>
</html>