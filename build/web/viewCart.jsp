<%@page import="model.CartDTO, java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
    <title>Your Shopping Cart</title>
    <style>
        table { border-collapse: collapse; width: 80%; }
        th, td { border: 1px solid black; padding: 10px; text-align: left; }
        .total { font-weight: bold; color: blue; }
    </style>
</head>
<body>
    <h1>Giỏ hàng của <%= ((model.UserDTO)session.getAttribute("LOGIN_USER")).getFullName() %></h1>
    
    <%
        List<CartDTO> list = (List<CartDTO>) request.getAttribute("CART_LIST");
        if (list != null && !list.isEmpty()) {
    %>
        <table>
            <thead>
                <tr>
                    <th>STT</th>
                    <th>Tên sản phẩm</th>
                    <th>Đơn giá</th>
                    <th>Số lượng</th>
                    <th>Thành tiền</th>
                </tr>
            </thead>
            <tbody>
                <%
                    int count = 1;
                    float total = 0;
                    for (CartDTO item : list) {
                        float subtotal = item.getQuantity() * item.getProduct().getPrice();
                        total += subtotal;
                %>
                <tr>
                    <td><%= count++ %></td>
                    <td><%= item.getProduct().getName() %></td>
                    <td><%= item.getProduct().getPrice() %></td>
                    <td><%= item.getQuantity() %></td>
                    <td><%= subtotal %></td>
                </tr>
                <% } %>
                <tr>
                    <td colspan="4" align="right" class="total">Tổng cộng:</td>
                    <td class="total"><%= total %></td>
                </tr>
            </tbody>
        </table>
    <%
        } else {
    %>
        <h3>Giỏ hàng của bạn đang trống!</h3>
    <% } %>
    
    <br/>
    <a href="MainController?action=Search&search=">Tiếp tục mua sắm</a>
</body>
</html>