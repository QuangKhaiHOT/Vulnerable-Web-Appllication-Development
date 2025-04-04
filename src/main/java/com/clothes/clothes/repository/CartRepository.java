package com.clothes.clothes.repository;

import com.clothes.clothes.config.Database;
import com.clothes.clothes.model.CartDetail;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CartRepository {
    //Xem giỏ hàng
        public static List<CartDetail> getCartDetails(int userId) {
            List<CartDetail> cartDetails = new ArrayList<>();
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT c.cart_id, u.username, p.name as product_name, " +
                            "cat.category_name, p.price, c.quantity, p.image_url " +
                            "FROM cart c " +
                            "JOIN user u ON c.user_id = u.id " +
                            "JOIN products p ON c.product_id = p.product_id " +
                            "JOIN categories cat ON p.category_id = cat.category_id " +
                            "WHERE c.user_id = " + userId;

                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    BigDecimal price = rs.getBigDecimal("price");
                    int quantity = rs.getInt("quantity");
                    BigDecimal totalPrice = price.multiply(new BigDecimal(quantity));

                    cartDetails.add(new CartDetail(
                        rs.getInt("cart_id"),
                        rs.getString("username"),
                        rs.getString("product_name"),
                        rs.getString("category_name"),
                        price,
                        quantity,
                        totalPrice,
                        rs.getString("image_url")
                    ));
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
            return cartDetails;
        }
    //Thêm sản phẩm vào giỏ hàng
    public static boolean addItemToCart(int userId, int productId, int quantity) {
        try (Connection conn = Database.getConnection()) {
            // Kiểm tra xem sản phẩm đã có trong giỏ hàng chưa
            String checkSql = "SELECT cart_id, quantity FROM cart WHERE user_id = " + userId + " AND product_id = " + productId;
            Statement checkStmt = conn.createStatement();
            ResultSet rs = checkStmt.executeQuery(checkSql);

            if (rs.next()) {
                // Nếu sản phẩm đã có trong giỏ hàng, cập nhật số lượng
                int cartId = rs.getInt("cart_id");
                int currentQuantity = rs.getInt("quantity");
                int newQuantity = currentQuantity + quantity;

                String updateSql = "UPDATE cart SET quantity = " + newQuantity + " WHERE cart_id = " + cartId;
                Statement updateStmt = conn.createStatement();
                int rowsUpdated = updateStmt.executeUpdate(updateSql);
                return rowsUpdated > 0;
            } else {
                // Nếu sản phẩm chưa có trong giỏ hàng, thêm mới
                String insertSql = "INSERT INTO cart (user_id, product_id, quantity) VALUES (" + userId + ", " + productId + ", " + quantity + ")";
                Statement insertStmt = conn.createStatement();
                int rowsInserted = insertStmt.executeUpdate(insertSql);
                return rowsInserted > 0;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
    //Xóa sản phẩm khỏi giỏ hàng// Thêm vào CartRepository
    public static boolean deleteCartItem(int cartId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM cart WHERE cart_id = " + cartId;
            Statement stmt = conn.createStatement();
            int rowsDeleted = stmt.executeUpdate(sql);
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
}
