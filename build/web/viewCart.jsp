<%@page import="model.CartDTO, java.util.List"%>
<%@page contentType="text/html" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
        <title>Your Shopping Cart</title>
        <style>
            table {
                border-collapse: collapse;
                width: 80%;
            }
            th, td {
                border: 1px solid black;
                padding: 10px;
                text-align: left;
            }
            .total {
                font-weight: bold;
                color: blue;
            }
        </style>
    </head>
    <body>
        <h1>Giỏ hàng của <%= ((model.UserDTO) session.getAttribute("LOGIN_USER")).getFullName()%></h1>

        <%
            List<CartDTO> list = (List<CartDTO>) request.getAttribute("CART_LIST");
            if (list != null && !list.isEmpty()) {
        %>
        <table>
            <thead>
                <tr>
                    <th>ID</th>
                    <th>Name</th>
                    <th>Price</th>
                    <th>Quantity</th>
                    <th>Total</th>
                    <th>Action</th>
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
                    <td><%= count++%></td>
                    <td><%= item.getProduct().getName()%></td>
                    <td><%= item.getProduct().getPrice()%></td>
                    <td><%= item.getQuantity()%></td>
                    <td><%= subtotal%></td>
                    <td>
                        <a href="CartController?action=Remove&id=<%= item.getProduct().getId()%>" 
                           onclick="return confirm('Bạn có chắc muốn xóa sản phẩm này không?');"
                           style="color: red; text-decoration: none;">
                            Xóa
                        </a>
                    </td>
                </tr>
                <% }%>
                <tr>
                    <td colspan="4" align="right" class="total">Total Price:</td>
                    <td class="total"><%= total%></td>
                </tr>
            </tbody>
        </table>
        <%
        } else {
        %>
        <h3>Giỏ hàng của bạn đang trống!</h3>
        <% }%>

        <br/>
        <a href="MainController?action=Search&search=">Tiếp tục mua sắm</a>
    </body>
</html>