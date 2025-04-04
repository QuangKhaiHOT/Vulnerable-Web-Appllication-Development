package com.clothes.clothes.repository;

import com.clothes.clothes.config.Database;
import com.clothes.clothes.model.Order;
import com.clothes.clothes.model.OrderDetail;

import java.io.Console;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class OrderRepository {
    //Tạo Order
  public static Order createOrderFromCart(int userId, String shippingAddress, String phone) {
      try (Connection conn = Database.getConnection()) {
          // Kiểm tra giỏ hàng có trống không
          String checkCartSql = "SELECT COUNT(*) FROM cart WHERE user_id = " + userId;
          Statement checkStmt = conn.createStatement();
          ResultSet checkRs = checkStmt.executeQuery(checkCartSql);
          if (checkRs.next() && checkRs.getInt(1) == 0) {
              return null;
          }

          conn.setAutoCommit(false);
          try {
              // 1. Tính tổng tiền từ giỏ hàng
              String totalSql = "SELECT COALESCE(SUM(p.price * c.quantity), 0) as total " +
                              "FROM cart c " +
                              "JOIN products p ON c.product_id = p.product_id " +
                              "WHERE c.user_id = " + userId;

              Statement totalStmt = conn.createStatement();
              ResultSet totalRs = totalStmt.executeQuery(totalSql);

              BigDecimal totalAmount = BigDecimal.ZERO;
              if (totalRs.next()) {
                  totalAmount = totalRs.getBigDecimal("total");
              }

              // 2. Tạo đơn hàng mới
              String createOrderSql = "INSERT INTO orders (user_id, total_amount, shipping_address, phone) " +
                                    "VALUES (" + userId + ", " + totalAmount + ", '" +
                                    shippingAddress + "', '" + phone + "')";

              Statement stmt = conn.createStatement();
              stmt.executeUpdate(createOrderSql, Statement.RETURN_GENERATED_KEYS);
              ResultSet rs = stmt.getGeneratedKeys();

              int orderId = 0;
              if (rs.next()) {
                  orderId = rs.getInt(1);
              }

              // 3. Thêm chi tiết đơn hàng từ giỏ hàng
              String createDetailsSql = "INSERT INTO order_details (order_id, product_id, quantity, price) " +
                                      "SELECT " + orderId + ", c.product_id, c.quantity, p.price " +
                                      "FROM cart c " +
                                      "JOIN products p ON c.product_id = p.product_id " +
                                      "WHERE c.user_id = " + userId;

              stmt.executeUpdate(createDetailsSql);

              conn.commit();
              return getOrderById(orderId);

          } catch (SQLException e) {
              conn.rollback();
              System.out.println("Transaction failed: " + e.getMessage());
              return null;
          }
      } catch (SQLException e) {
          System.out.println("Database error: " + e.getMessage());
          return null;
      }
  }
  public static Order getOrderById(int orderId) {
      try (Connection conn = Database.getConnection()) {
          // 1. Lấy thông tin đơn hàng
          String orderSql = "SELECT o.*, u.username " +
                           "FROM orders o " +
                           "INNER JOIN user u ON o.user_id = u.id " +
                           "WHERE o.order_id = " + orderId;

          Statement stmt = conn.createStatement();
          ResultSet rs = stmt.executeQuery(orderSql);

          if (rs.next()) {
              Order order = new Order();
              order.setOrderId(rs.getInt("order_id"));
              order.setUserId(rs.getInt("user_id"));
              order.setUsername(rs.getString("username"));
              order.setTotalAmount(rs.getBigDecimal("total_amount"));
              order.setShippingAddress(rs.getString("shipping_address"));
              order.setPhone(rs.getString("phone"));

              // 2. Lấy chi tiết đơn hàng
              String detailsSql = "SELECT od.*, p.name as product_name, p.image_url " +
                                "FROM order_details od " +
                                "INNER JOIN products p ON od.product_id = p.product_id " +
                                "WHERE od.order_id = " + orderId;

              ResultSet detailsRs = stmt.executeQuery(detailsSql);
              List<OrderDetail> details = new ArrayList<>();

              while (detailsRs.next()) {
                  OrderDetail detail = new OrderDetail();
                  detail.setOrderDetailId(detailsRs.getInt("order_detail_id"));
                  detail.setOrderId(detailsRs.getInt("order_id"));
                  detail.setProductName(detailsRs.getString("product_name"));
                  detail.setImageUrl(detailsRs.getString("image_url"));
                  detail.setQuantity(detailsRs.getInt("quantity"));
                  detail.setPrice(detailsRs.getBigDecimal("price"));
                  detail.setTotalPrice(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
                  details.add(detail);
              }

              order.setOrderDetails(details);
              return order;
          }
      } catch (SQLException e) {
          System.out.println("Database error: " + e.getMessage());
      }
      return null;
  }
  //Lấy tất cả đơn hàng của user
    public static List<Order> getOrdersByUserId(int userId) {
        List<Order> orders = new ArrayList<>();
        try (Connection conn = Database.getConnection()) {
            // 1. Lấy tất cả đơn hàng của user
            String orderSql = "SELECT o.*, u.username " +
                             "FROM orders o " +
                             "INNER JOIN user u ON o.user_id = u.id " +
                             "WHERE o.user_id = " + userId + " " +
                             "ORDER BY o.order_id DESC";

            Statement stmt = conn.createStatement();
            ResultSet rs = stmt.executeQuery(orderSql);

            while (rs.next()) {
                Order order = new Order();
                int orderId = rs.getInt("order_id");

                order.setOrderId(orderId);
                order.setUserId(rs.getInt("user_id"));
                order.setUsername(rs.getString("username"));
                order.setTotalAmount(rs.getBigDecimal("total_amount"));
                order.setShippingAddress(rs.getString("shipping_address"));
                order.setPhone(rs.getString("phone"));

                // 2. Lấy chi tiết của từng đơn hàng
                String detailsSql = "SELECT od.*, p.name as product_name, p.image_url " +
                                  "FROM order_details od " +
                                  "INNER JOIN products p ON od.product_id = p.product_id " +
                                  "WHERE od.order_id = " + orderId;

                ResultSet detailsRs = stmt.executeQuery(detailsSql);
                List<OrderDetail> details = new ArrayList<>();

                while (detailsRs.next()) {
                    OrderDetail detail = new OrderDetail();
                    detail.setOrderDetailId(detailsRs.getInt("order_detail_id"));
                    detail.setOrderId(detailsRs.getInt("order_id"));
                    detail.setProductName(detailsRs.getString("product_name"));
                    detail.setImageUrl(detailsRs.getString("image_url"));
                    detail.setQuantity(detailsRs.getInt("quantity"));
                    detail.setPrice(detailsRs.getBigDecimal("price"));
                    detail.setTotalPrice(detail.getPrice().multiply(BigDecimal.valueOf(detail.getQuantity())));
                    details.add(detail);
                }

                order.setOrderDetails(details);
                orders.add(order);
            }
        } catch (SQLException e) {
            System.out.println("Database error: " + e.getMessage());
        }
        return orders;
    }
}
