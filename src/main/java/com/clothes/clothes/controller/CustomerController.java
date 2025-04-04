package com.clothes.clothes.controller;

import com.clothes.clothes.model.CartDetail;
import com.clothes.clothes.model.Order;
import com.clothes.clothes.model.User;
import com.clothes.clothes.repository.CartRepository;
import com.clothes.clothes.repository.OrderRepository;
import com.clothes.clothes.repository.ProductsRepository;
import com.clothes.clothes.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/customer")
public class CustomerController {
    //****Người Dùng****//
    //Xem thông tin cá nhân
    @GetMapping("/profile/{id}")
    public ResponseEntity<?> getUserProfile(@PathVariable long id) {
        User user = UserRepository.findUserById(id);
        if (user != null) {
            return new ResponseEntity<>(user, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("User not found", HttpStatus.NOT_FOUND);
        }
    }
    //Upload file avt
    @PostMapping("/upload")
    public ResponseEntity<Map<String, String>> handleFileUpload( @RequestParam("userId") Long userId,@RequestParam("file") MultipartFile file) {
        try {
            // Đường dẫn thư mục uploads
            String uploadDir = "uploads";
            Path uploadPath = Paths.get(uploadDir);

            // Tạo thư mục nếu chưa tồn tại
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }

            // Lấy đường dẫn ảnh cũ từ database dựa vào userId
            User userInfo = UserRepository.findUserById(userId);
            String avatarPath = userInfo.getAvtUrl();

            // Loại bỏ ký tự '/' ở đầu (tránh lỗi khi tạo đường dẫn)
            if (avatarPath.startsWith("/")) {
                avatarPath = avatarPath.substring(1);
            }
            // Đường dẫn tuyệt đối đến file ảnh
            Path url = Paths.get(System.getProperty("user.dir"), avatarPath);
            // Xóa ảnh cũ
            if (Files.exists(url)) {
                Files.delete(url);
            }
            String fileName = file.getOriginalFilename();
            Path filePath = uploadPath.resolve(fileName);
            // Lưu file mới
            Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

            Map<String, String> response = new HashMap<>();
            response.put("url", "/uploads/" + fileName);
            return ResponseEntity.ok(response);

        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    // Cập nhật thông tin cá nhân
    @PostMapping("/update")
    public ResponseEntity<?> updateUser(@RequestBody Map<String, Object> payload) {
        try {
            Long id = Long.parseLong(payload.get("id").toString());
            String username = (String) payload.get("username");
            String password = (String) payload.get("password");
            String avtUrl = (String) payload.get("avtUrl");

            boolean success = UserRepository.updateProfile(id, username, password, avtUrl);

            if (success) {
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
            }
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }
    //****Giỏ Hàng****//
    // Xem giỏ hàng của user
    @GetMapping("/cart/{userId}")
    public ResponseEntity<?> getCartDetails(@PathVariable int userId) {
        List<CartDetail> cartDetails = CartRepository.getCartDetails(userId);
        if (!cartDetails.isEmpty()) {
            return new ResponseEntity<>(cartDetails, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Giỏ hàng trống", HttpStatus.NOT_FOUND);
        }
    }
    // Thêm sản phẩm vào giỏ hàng
    @PostMapping("/cart/add")
    public ResponseEntity<?> addItemToCart(
            @RequestParam int userId,
            @RequestParam int productId,
            @RequestParam int quantity) {

        // Kiểm tra sản phẩm có tồn tại không
        if (!ProductsRepository.isProductExistsById(productId)) {
            return new ResponseEntity<>("Sản phẩm không tồn tại", HttpStatus.NOT_FOUND);
        }

        // Kiểm tra số lượng hợp lệ
        if (quantity <= 0) {
            return new ResponseEntity<>("Số lượng không hợp lệ", HttpStatus.BAD_REQUEST);
        }

        boolean success = CartRepository.addItemToCart(userId, productId, quantity);
        if (success) {
            return new ResponseEntity<>("Thêm vào giỏ hàng thành công", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Thêm vào giỏ hàng thất bại", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
    // Xóa sản phẩm khỏi giỏ hàng
    @DeleteMapping("/cart/delete/{cartId}")
    public ResponseEntity<?> deleteCartItem(@PathVariable int cartId) {
        boolean success = CartRepository.deleteCartItem(cartId);
        if (success) {
            return new ResponseEntity<>("Đã xóa sản phẩm khỏi giỏ hàng", HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không thể xóa sản phẩm khỏi giỏ hàng", HttpStatus.NOT_FOUND);
        }
    }

    //****Đơn Hàng****//
    // Xem tất cả đơn hàng của user
    @GetMapping("/orders/{userId}")
    public ResponseEntity<?> getOrdersByUserId(@PathVariable int userId) {
        List<Order> orders = OrderRepository.getOrdersByUserId(userId);
        if (!orders.isEmpty()) {
            return new ResponseEntity<>(orders, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không có đơn hàng nào", HttpStatus.NOT_FOUND);
        }
    }


    // Xem chi tiết một đơn hàng
    @GetMapping("/order/{orderId}")
    public ResponseEntity<?> getOrderById(@PathVariable int orderId) {
        Order order = OrderRepository.getOrderById(orderId);
        if (order != null) {
            return new ResponseEntity<>(order, HttpStatus.OK);
        } else {
            return new ResponseEntity<>("Không tìm thấy đơn hàng", HttpStatus.NOT_FOUND);
        }
    }

    // Tạo đơn hàng mới từ giỏ hàng
    @PostMapping("/order/create")
    public ResponseEntity<?> createOrder(
            @RequestParam int userId,
            @RequestParam String shippingAddress,
            @RequestParam String phone) {
        // Kiểm tra giỏ hàng có trống không
        List<CartDetail> cart = CartRepository.getCartDetails(userId);
        if (cart.isEmpty()) {
            return new ResponseEntity<>("Giỏ hàng trống", HttpStatus.BAD_REQUEST);
        }

        Order newOrder = OrderRepository.createOrderFromCart(userId, shippingAddress, phone);
        if (newOrder != null) {
            // Xóa giỏ hàng sau khi tạo đơn hàng thành công
            //CartRepository.clearCart(userId);
            return new ResponseEntity<>(newOrder, HttpStatus.CREATED);
        } else {
            return new ResponseEntity<>("Không thể tạo đơn hàng", HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }
}
