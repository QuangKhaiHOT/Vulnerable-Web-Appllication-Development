package com.clothes.clothes.controller;

import com.clothes.clothes.model.Categories;
import com.clothes.clothes.model.Products;
import com.clothes.clothes.model.User;
import com.clothes.clothes.repository.CategoriesRepository;
import com.clothes.clothes.repository.ProductsRepository;
import com.clothes.clothes.repository.UserRepository;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    // ====== QUẢN LÝ DANH MỤC ======
    // Lấy danh sách danh mục
    @GetMapping("/categories")
    public List<Categories> getAllCategories() {
        return CategoriesRepository.findAll();
    }
    // Thêm danh mục mới
    @PostMapping("/categories/add")
    public ResponseEntity<String> themDanhMuc(@RequestParam String categoryName) {
        if (categoryName == null || categoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên danh mục không được để trống!");
        }

        // Kiểm tra danh mục đã tồn tại chưa
        if (CategoriesRepository.isCategoryExists(categoryName)) {
            return ResponseEntity.badRequest().body("Danh mục đã tồn tại!");
        }

        boolean thanhCong = CategoriesRepository.addCategory(categoryName);
        if (thanhCong) {
            return ResponseEntity.ok("Thêm danh mục thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm danh mục.");
        }
    }
    @DeleteMapping("/categories/delete/{id}")
    public ResponseEntity<String> xoaDanhMuc(@PathVariable int id) {
        if (!CategoriesRepository.isCategoryExistsById(id)) {
            return ResponseEntity.badRequest().body("Danh mục không tồn tại!");
        }

        boolean thanhCong = CategoriesRepository.deleteCategory(id);
        if (thanhCong) {
            return ResponseEntity.ok("Xóa danh mục thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa danh mục.");
        }
    }
    // Thêm endpoint để cập nhật danh mục
    @PutMapping("/categories/update/{id}")
    public ResponseEntity<String> capNhatDanhMuc(@PathVariable int id, @RequestParam String newCategoryName) {
        if (newCategoryName == null || newCategoryName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên danh mục không được để trống!");
        }

        if (!CategoriesRepository.isCategoryExistsById(id)) {
            return ResponseEntity.badRequest().body("Danh mục không tồn tại!");
        }

        boolean thanhCong = CategoriesRepository.updateCategory(id, newCategoryName);
        if (thanhCong) {
            return ResponseEntity.ok("Cập nhật danh mục thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật danh mục.");
        }
    }
    // Tìm kiếm danh mục theo tên
    @GetMapping("/categories/search")
    public ResponseEntity<List<Categories>> searchCategoriesByName(@RequestParam String name) {
        List<Categories> categories = CategoriesRepository.findByName(name);
        return ResponseEntity.ok(categories);
    }

    // ====== QUẢN LÝ SẢN PHẨM ======
    @GetMapping("/products")
    public List<Products> getAllProducts() {
        return ProductsRepository.findAll();
    }

    @PostMapping("/products/add")
    public ResponseEntity<String> themSanPham(@RequestParam String name, @RequestParam BigDecimal price, @RequestParam int stock, @RequestParam int categoryId, @RequestParam String imageUrl) {
        if (name == null || name.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên sản phẩm không được để trống!");
        }

        // Kiểm tra sản phẩm đã tồn tại chưa
        if (ProductsRepository.isProductExists(name)) {
            return ResponseEntity.badRequest().body("Sản phẩm đã tồn tại!");
        }

        boolean thanhCong = ProductsRepository.addProduct(name, price, stock, categoryId, imageUrl);
        if (thanhCong) {
            return ResponseEntity.ok("Thêm sản phẩm thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm sản phẩm.");
        }
    }

    @DeleteMapping("/products/delete/{id}")
    public ResponseEntity<String> xoaSanPham(@PathVariable int id) {
        if (!ProductsRepository.isProductExistsById(id)) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }

        boolean thanhCong = ProductsRepository.deleteProduct(id);
        if (thanhCong) {
            return ResponseEntity.ok("Xóa sản phẩm thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa sản phẩm.");
        }
    }

    @PutMapping("/products/update/{id}")
    public ResponseEntity<String> capNhatSanPham(@PathVariable int id, @RequestParam String newName, @RequestParam BigDecimal newPrice, @RequestParam int newStock, @RequestParam int newCategoryId, @RequestParam String newImageUrl) {
        if (newName == null || newName.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Tên sản phẩm không được để trống!");
        }

        if (!ProductsRepository.isProductExistsById(id)) {
            return ResponseEntity.badRequest().body("Sản phẩm không tồn tại!");
        }

        boolean thanhCong = ProductsRepository.updateProduct(id, newName, newPrice, newStock, newCategoryId, newImageUrl);
        if (thanhCong) {
            return ResponseEntity.ok("Cập nhật sản phẩm thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật sản phẩm.");
        }
    }

    @GetMapping("/products/search")
    public ResponseEntity<List<Products>> searchProductsByName(@RequestParam String name) {
        List<Products> products = ProductsRepository.findByName(name);
        return ResponseEntity.ok(products);
    }

    // ====== QUẢN LÝ USER ======
    // Lấy danh sách user
    @GetMapping("/users")
    public List<User> getAllUsers() {
        return UserRepository.findAll();
    }

    // Thêm user mới
    @PostMapping("/users/add")
    public ResponseEntity<String> themUser(@RequestParam String username, @RequestParam String password, @RequestParam String email) {
        if (username == null || username.trim().isEmpty() || password == null || password.trim().isEmpty() || email == null || email.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Thông tin user không được để trống!");
        }

        // Kiểm tra email đã tồn tại chưa
        if (UserRepository.emailExists(email)) {
            return ResponseEntity.badRequest().body("Email đã tồn tại!");
        }

        boolean thanhCong = UserRepository.addUser(username, password, email);
        if (thanhCong) {
            return ResponseEntity.ok("Thêm user thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi thêm user.");
        }
    }

    // Xóa user theo ID
    @DeleteMapping("/users/delete/{id}")
    public ResponseEntity<String> xoaUser(@PathVariable long id) {
        if (!UserRepository.isUserExistsById(id)) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        boolean thanhCong = UserRepository.deleteUserById(id);
        if (thanhCong) {
            return ResponseEntity.ok("Xóa user thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi xóa user.");
        }
    }

    // Cập nhật user
    @PutMapping("/users/update/{id}")
    public ResponseEntity<String> capNhatUser(@PathVariable long id, @RequestParam String newUsername, @RequestParam String newPassword, @RequestParam String newEmail) {
        if (newUsername == null || newUsername.trim().isEmpty() || newPassword == null || newPassword.trim().isEmpty() || newEmail == null || newEmail.trim().isEmpty()) {
            return ResponseEntity.badRequest().body("Thông tin user không được để trống!");
        }

        if (!UserRepository.isUserExistsById(id)) {
            return ResponseEntity.badRequest().body("User không tồn tại!");
        }

        boolean thanhCong = UserRepository.updateUser(id, newUsername, newPassword, newEmail);
        if (thanhCong) {
            return ResponseEntity.ok("Cập nhật user thành công!");
        } else {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Lỗi khi cập nhật user.");
        }
    }

    // Tìm kiếm user theo tên
    @GetMapping("/users/search")
    public ResponseEntity<List<User>> searchUsersByName(@RequestParam String username) {
        List<User> users = UserRepository.findUsersByName(username);
        return ResponseEntity.ok(users);
    }
}
