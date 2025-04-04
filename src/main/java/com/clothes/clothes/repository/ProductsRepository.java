package com.clothes.clothes.repository;

import com.clothes.clothes.config.Database;
import com.clothes.clothes.model.Products;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class ProductsRepository {
        // Lấy danh sách Products
        public static List<Products> findAll() {
            List<Products> products = new ArrayList<>();
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT * FROM products";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);

                while (rs.next()) {
                    products.add(new Products(
                            rs.getLong("product_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("stock"),
                            CategoriesRepository.findById(rs.getInt("category_id")),
                            rs.getString("image_url")
                    ));
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
            return products;
        }

        // Kiểm tra Product
        public static boolean isProductExists(String productName) {
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT COUNT(*) FROM products WHERE name = '" + productName + "'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu COUNT > 0 thì product đã tồn tại
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
            return false;
        }

        // Thêm Product
        public static boolean addProduct(String name, BigDecimal price, int stock, int categoryId, String imageUrl) {
            try (Connection conn = Database.getConnection()) {
                String sql = "INSERT INTO products (name, price, stock, category_id, image_url) VALUES ('" + name + "', " + price + ", " + stock + ", " + categoryId + ", '" + imageUrl + "')";
                Statement stmt = conn.createStatement();
                int rowsInserted = stmt.executeUpdate(sql);
                return rowsInserted > 0;
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return false;
            }
        }

        // Kiểm tra ID Product
        public static boolean isProductExistsById(int id) {
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT COUNT(*) FROM products WHERE product_id = " + id;
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                if (rs.next()) {
                    return rs.getInt(1) > 0; // Nếu COUNT > 0 thì product tồn tại
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
            return false;
        }

        // Xóa Product theo ID
        public static boolean deleteProduct(int id) {
            try (Connection conn = Database.getConnection()) {
                String sql = "DELETE FROM products WHERE product_id = " + id;
                Statement stmt = conn.createStatement();
                int rowsDeleted = stmt.executeUpdate(sql);
                return rowsDeleted > 0; // Nếu có dòng bị xóa => thành công
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
            return false;
        }

        // Chỉnh sửa Product
        public static boolean updateProduct(int productId, String newName, BigDecimal newPrice, int newStock, int newCategoryId, String newImageUrl) {
            try (Connection conn = Database.getConnection()) {
                String sql = "UPDATE products SET name = '" + newName + "', price = " + newPrice + ", stock = " + newStock + ", category_id = " + newCategoryId + ", image_url = '" + newImageUrl + "' WHERE product_id = " + productId;
                Statement stmt = conn.createStatement();
                int rowsUpdated = stmt.executeUpdate(sql);
                return rowsUpdated > 0;
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
                return false;
            }
        }

        // Tìm Product theo tên
        public static List<Products> findByName(String name) {
            List<Products> products = new ArrayList<>();
            try (Connection conn = Database.getConnection()) {
                String sql = "SELECT * FROM products WHERE name LIKE '%" + name + "%'";
                Statement stmt = conn.createStatement();
                ResultSet rs = stmt.executeQuery(sql);
                while (rs.next()) {
                    products.add(new Products(
                            rs.getLong("product_id"),
                            rs.getString("name"),
                            rs.getBigDecimal("price"),
                            rs.getInt("stock"),
                            CategoriesRepository.findById(rs.getInt("category_id")),
                            rs.getString("image_url")
                    ));
                }
            } catch (SQLException e) {
                System.out.println("Database error: " + e.getMessage());
            }
            return products;
        }
}
