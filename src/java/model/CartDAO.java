package model;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class CartDAO {
    // Thêm hoặc cập nhật số lượng sản phẩm trong giỏ
    public boolean addToCart(String userID, String productID, int quantity) throws Exception {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                // Sử dụng logic: Nếu tồn tại thì cộng dồn số lượng, nếu không thì chèn mới
                String sql = "IF EXISTS (SELECT * FROM tblCart WHERE userID=? AND productID=?) "
                           + "UPDATE tblCart SET quantity = quantity + ? WHERE userID=? AND productID=? "
                           + "ELSE INSERT INTO tblCart(userID, productID, quantity) VALUES(?,?,?)";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, userID);
                ptm.setString(2, productID);
                ptm.setInt(3, quantity);
                ptm.setString(4, userID);
                ptm.setString(5, productID);
                ptm.setString(6, userID);
                ptm.setString(7, productID);
                ptm.setInt(8, quantity);
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }

    // Lấy danh sách giỏ hàng kèm thông tin sản phẩm
    public List<CartDTO> getCart(String userID) throws Exception {
        List<CartDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ptm = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            String sql = "SELECT c.cartID, c.quantity, p.id, p.name, p.price, p.category "
                       + "FROM tblCart c JOIN tblProducts p ON c.productID = p.id WHERE c.userID=?";
            ptm = conn.prepareStatement(sql);
            ptm.setString(1, userID);
            rs = ptm.executeQuery();
            while (rs.next()) {
                ProductDTO p = new ProductDTO(rs.getString("id"), rs.getString("name"), 
                                            rs.getString("category"), rs.getFloat("price"), 0);
                list.add(new CartDTO(rs.getInt("cartID"), userID, p, rs.getInt("quantity")));
            }
        } finally {
            if (rs != null) rs.close();
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return list;
    }
    
    // Xóa sản phẩm khỏi giỏ hàng
    public boolean delete(String userID, String productID) throws Exception {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ptm = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                String sql = "DELETE FROM tblCart WHERE userID=? AND productID=?";
                ptm = conn.prepareStatement(sql);
                ptm.setString(1, userID);
                ptm.setString(2, productID);
                check = ptm.executeUpdate() > 0;
            }
        } finally {
            if (ptm != null) ptm.close();
            if (conn != null) conn.close();
        }
        return check;
    }
}