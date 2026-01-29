package model;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import utils.DBUtils;

public class UserDAO {
    private static final String LOGIN = "SELECT fullName, roleID FROM tblUsers WHERE userID = ? AND password = ?";

    public UserDTO login(String userID, String password) throws SQLException {
        UserDTO user = null;
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        try {
            conn = DBUtils.getConnection();
            if (conn != null) {
                ps = conn.prepareStatement(LOGIN);
                ps.setString(1, userID);
                ps.setString(2, password);
                rs = ps.executeQuery();
                if (rs.next()) {
                    String fullName = rs.getString("fullName");
                    int roleID = rs.getInt("roleID");
                    user = new UserDTO(userID, fullName, password);
                    user.setRoleID(roleID);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (rs != null) rs.close();
            if (ps != null) ps.close();
            if (conn != null) conn.close();
        }
        return user;
    }
}