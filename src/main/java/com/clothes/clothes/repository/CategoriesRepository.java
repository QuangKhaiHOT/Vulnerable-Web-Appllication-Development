package com.clothes.clothes.repository;

import com.clothes.clothes.config.Database;
import com.clothes.clothes.model.Categories;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class CategoriesRepository {
    //Lấy danh sách Category
    public static List<Categories> findAll() {
        List<Categories> categories = new ArrayList<Categories>();
        try (Connection con = Database.getConnection()) {
            String sql = "SELECT * FROM categories";
            Statement stmt = con.createStatement();
            ResultSet rs = stmt.executeQuery(sql);

            while (rs.next()) {
                categories.add(new Categories(rs.getLong("category_id"), rs.getString("category_name")));
            }
            return categories;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return categories;
    }

    //Kiểm tra Category
    public static boolean isCategoryExists(String categoryName) {

        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM categories WHERE category_name = '" + categoryName + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu COUNT > 0 thì category đã tồn tại
            }

        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    //Thêm Category
    public static Boolean addCategory(String categoryName) {
        try (Connection conn = Database.getConnection()) {
            String sql = "INSERT INTO categories (category_name) VALUES ('" + categoryName + "')";
            Statement stmt = conn.createStatement();
            int rowsInserted = stmt.executeUpdate(sql);
            return rowsInserted > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }

    //Kiểm tra ID Category
    public static boolean isCategoryExistsById(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM categories WHERE category_id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0; // Nếu COUNT > 0 thì danh mục tồn tại
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }

    //Xóa Category theo ID
    public static boolean deleteCategory(int id) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM categories WHERE category_id = " + id;
            Statement stmt = conn.createStatement();
            int rowsDeleted = stmt.executeUpdate(sql);
            return rowsDeleted > 0; // Nếu có dòng bị xóa => thành công
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
    //Chỉnh sửa danh mục
    public static boolean updateCategory(int categoryId, String newCategoryName) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE categories SET category_name = '" + newCategoryName + "' WHERE category_id = " + categoryId;
            Statement stmt = conn.createStatement();

            int rowsUpdated = stmt.executeUpdate(sql);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Lỗi khi cập nhật danh mục: " + e.getMessage());
            return false;
        }
    }
    // Tìm danh mục theo tên
    public static List<Categories> findByName(String name) {
        List<Categories> categories = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM categories WHERE category_name LIKE '%" + name + "%'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                categories.add(new Categories(rs.getLong("category_id"), rs.getString("category_name")));
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return categories;
    }
    // Tìm danh mục theo ID
    public static Categories findById(int id) {
        Categories category = null;
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM categories WHERE category_id = " + id;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                category = new Categories(rs.getLong("category_id"), rs.getString("category_name"));
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return category;
    }
}

