package com.clothes.clothes.repository;

import com.clothes.clothes.config.Database;
import com.clothes.clothes.model.User;

import java.sql.*;
import java.util.List;
import java.util.ArrayList;

public class UserRepository {
    //Lấy thông tin user bằng email
    public static User getUserByEmail(String email) {
        //Lấy thông tin user
        try(Connection conn= Database.getConnection()){
            String sql = "SELECT * FROM user WHERE email = '" + email + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("avt_path")
                );

            }
            return null;
        }catch (SQLException e){
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
    public static User loginUser(String email, String password) {
        //Lấy thông tin user
        try(Connection conn= Database.getConnection()){
            String sql = "SELECT * FROM user WHERE email = '" + email + "' AND password = '" + password + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if(rs.next()){
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("avt_path")
                );

            }
            return null;
        }catch (SQLException e){
            System.out.println("Database error: " + e.getMessage());
            return null;
        }
    }
    public static boolean updateProfile(Long id, String username, String password, String avtUrl) {
        try (Connection conn = Database.getConnection()) {
            String sql;
            if (avtUrl != null && !avtUrl.isEmpty()) {
                sql = "UPDATE user SET username = '" + username + "', password = '" + password +
                      "', avt_path = '" + avtUrl + "' WHERE id = " + id;
            } else {
                sql = "UPDATE user SET username = '" + username + "', password = '" + password +
                      "' WHERE id = " + id;
            }

            Statement stmt = conn.createStatement();
            int rowsUpdated = stmt.executeUpdate(sql);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
    // Kiểm tra email đã tồn tại hay chưa
    public static boolean emailExists(String email) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM user WHERE email = '" + email + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " +e.getMessage());
        }
        return false;
    }
    public static boolean usernameExists(String username) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM user WHERE username = '" + username + "'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    //Tạo user mới
    public static boolean addUser(String username,String password,String email){
        try(Connection conn= Database.getConnection()){
            String sql="INSERT INTO user (username, password, role, email) VALUES ('"+ username + "', '" + password + "', 'client','" + email + "')";
            Statement stmt = conn.createStatement();
            int rowsInserted = stmt.executeUpdate(sql);
            return  rowsInserted>0;
        }catch(SQLException e){
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
    //Xóa User
    public static boolean deleteUserById(long userId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "DELETE FROM user WHERE id = " + userId;
            Statement stmt = conn.createStatement();
            int rowsDeleted = stmt.executeUpdate(sql);
            return rowsDeleted > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
    //Sửa User
    public static boolean updateUser(long userId, String newUsername, String newPassword, String newEmail) {
        try (Connection conn = Database.getConnection()) {
            String sql = "UPDATE user SET username = '" + newUsername + "', password = '" + newPassword + "', email = '" + newEmail + "' WHERE id = " + userId;
            Statement stmt = conn.createStatement();
            int rowsUpdated = stmt.executeUpdate(sql);
            return rowsUpdated > 0;
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
            return false;
        }
    }
    //Tim User
    public static List<User> findUsersByName(String username) {
        List<User> users = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM user WHERE username LIKE '%" + username + "%'";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("avt_path")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return users;
    }
    //Kiểm tra idUser có tồn tại không
    public static boolean isUserExistsById(long userId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT COUNT(*) FROM user WHERE id = " + userId;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return rs.getInt(1) > 0;
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return false;
    }
    //Lấy danh sách user
    public static List<User> findAll() {
        List<User> users = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM user";
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            while (rs.next()) {
                users.add(new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("avt_path")
                ));
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return users;
    }
    //Lấy thông tin User bằng id
    public static User findUserById(long userId) {
        try (Connection conn = Database.getConnection()) {
            String sql = "SELECT * FROM user WHERE id = " + userId;
            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(sql);
            if (rs.next()) {
                return new User(
                        rs.getLong("id"),
                        rs.getString("username"),
                        rs.getString("password"),
                        rs.getString("role"),
                        rs.getString("email"),
                        rs.getString("avt_path")
                );
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return null;
    }
}
