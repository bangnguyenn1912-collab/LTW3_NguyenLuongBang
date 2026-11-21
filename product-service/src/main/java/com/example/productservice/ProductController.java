package com.example.productservice;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

/**
 * Controller quản lý và cung cấp API cho Product Service.
 * Chạy trên cổng 8081.
 */
@CrossOrigin(origins = "*") // Cho phép Frontend gọi REST API từ các origin khác nhau (Live Server 5500)
@RestController
@RequestMapping("/api/products")
public class ProductController {

    // Danh sách sản phẩm mẫu theo yêu cầu tài liệu Lab 01
    private final List<Map<String, Object>> products = List.of(
        Map.of("id", 1, "name", "Mouse", "price", 150000),
        Map.of("id", 2, "name", "Keyboard", "price", 250000),
        Map.of("id", 3, "name", "Monitor", "price", 2500000)
    );

    /**
     * API 1: Lấy toàn bộ danh sách sản phẩm
     * GET /api/products
     * @return Danh sách sản phẩm (JSON)
     */
    @GetMapping
    public List<Map<String, Object>> getProducts() {
        return products;
    }

    /**
     * API 2: Tìm sản phẩm theo ID
     * GET /api/products/{id}
     * - Nếu tìm thấy: trả về HTTP 200 OK cùng dữ liệu sản phẩm
     * - Nếu không tìm thấy: trả về HTTP 404 Not Found
     * @param id Mã sản phẩm
     * @return ResponseEntity chứa sản phẩm hoặc HTTP 404
     */
    @GetMapping("/{id}")
    public ResponseEntity<Map<String, Object>> getProduct(@PathVariable int id) {
        // Sử dụng Stream API để lọc và tìm kiếm theo ID
        return products.stream()
                .filter(p -> p.get("id").equals(id))
                .findFirst()
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }
}
