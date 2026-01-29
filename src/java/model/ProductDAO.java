package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import utils.DBUtils;

public class ProductDAO {

    // Tao product
    private static final String CREATE = "INSERT INTO tblProducts(id, name, category, price, stockQuantity) VALUES(?,?,?,?,?)";
    // Tìm kiếm theo tên (gần đúng)
    private static final String SEARCH = "SELECT * FROM tblProducts WHERE name LIKE ?";
    // Xóa sản phẩm
    private static final String DELETE = "DELETE FROM tblProducts WHERE id = ?";
    // Cập nhật sản phẩm
    private static final String UPDATE = "UPDATE tblProducts SET name = ?, category = ?, price = ?, stockQuantity = ? WHERE id = ?";
    // Lấy 1 sản phẩm để hiển thị lên form Update (Bổ sung cho Requirement 5)
    private static final String GET_ONE = "SELECT * FROM tblProducts WHERE id = ?";

    public List<ProductDTO> searchByName(String search) throws SQLException {
        List<ProductDTO> list = new ArrayList<>();
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(SEARCH);
                ps.setString(1, "%" + search + "%");
                rs = ps.executeQuery();
                while (rs.next()) {
                    list.add(new ProductDTO(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getFloat("price"),
                            rs.getInt("stockQuantity")
                    ));
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return list;
    }

    public boolean delete(String id) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(DELETE);
                ps.setString(1, id);
                check = ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    public boolean update(ProductDTO product) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(UPDATE);
                ps.setString(1, product.getName());
                ps.setString(2, product.getCategory());
                ps.setFloat(3, product.getPrice());
                ps.setInt(4, product.getStockQuantity());
                ps.setString(5, product.getId());
                check = ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

    // Hàm phụ trợ để lấy thông tin sản phẩm cho trang updateProduct.jsp
    public ProductDTO getProductByID(String id) throws SQLException {
        ProductDTO product = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(GET_ONE);
                ps.setString(1, id);
                rs = ps.executeQuery();
                if (rs.next()) {
                    product = new ProductDTO(
                            rs.getString("id"),
                            rs.getString("name"),
                            rs.getString("category"),
                            rs.getFloat("price"),
                            rs.getInt("stockQuantity")
                    );
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return product;
    }

    public static String getNextID() throws SQLException {
        String nextID = "P001";
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                // Sử dụng ISNULL để tránh lỗi khi bảng trống và lọc các ID đúng định dạng P...
                String sql = "SELECT MAX(CAST(SUBSTRING(id, 2, 10) AS INT)) "
                        + "FROM tblProducts WHERE id LIKE 'P%'";
                ps = conn.prepareStatement(sql);
                rs = ps.executeQuery();
                if (rs.next()) {
                    int maxNumber = rs.getInt(1);
                    if (maxNumber >= 0) { // Nếu bảng có dữ liệu, tăng lên 1
                        nextID = String.format("P%03d", maxNumber + 1);
                    }
                }
            }
        } catch (Exception e) {
            // Log lỗi ra console để debug, nhưng ném ngoại lệ lên Controller xử lý
            e.printStackTrace();
            throw new SQLException("Error generating next ID: " + e.getMessage());
        } finally {
            if (rs != null) {
                rs.close();
            }
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return nextID;
    }

    public boolean create(ProductDTO product) throws SQLException {
        boolean check = false;
        Connection conn = null;
        PreparedStatement ps = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(CREATE);
                ps.setString(1, product.getId());
                ps.setString(2, product.getName());
                ps.setString(3, product.getCategory());
                ps.setFloat(4, product.getPrice());
                ps.setInt(5, product.getStockQuantity());
                check = ps.executeUpdate() > 0;
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (ps != null) {
                ps.close();
            }
            if (conn != null) {
                conn.close();
            }
        }
        return check;
    }

}
