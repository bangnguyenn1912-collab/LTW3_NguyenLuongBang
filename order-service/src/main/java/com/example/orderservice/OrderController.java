package com.example.orderservice;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.ResourceAccessException;
import org.springframework.web.client.RestClient;

import java.util.List;
import java.util.Map;

/**
 * Controller quản lý đơn hàng và giao tiếp với Product Service qua RestClient.
 * Chạy trên cổng 8082.
 */
@CrossOrigin(origins = "*") // Cho phép Frontend gọi API từ Live Server (cổng 5500)
@RestController
@RequestMapping("/api/orders")
public class OrderController {

    // RestClient của Spring dùng để gọi HTTP request đến các microservice khác
    private final RestClient restClient;

    // Danh sách đơn hàng mẫu theo tài liệu Lab 01
    private final List<Map<String, Object>> orders = List.of(
        Map.of("id", 101, "productId", 1, "quantity", 2),
        Map.of("id", 102, "productId", 2, "quantity", 1)
    );

    /**
     * Khởi tạo RestClient với Base URL của Product Service (http://localhost:8081)
     * Spring sẽ tự động inject RestClient.Builder vào constructor.
     */
    public OrderController(RestClient.Builder builder) {
        this.restClient = builder
                .baseUrl("http://localhost:8081")
                .build();
    }

    /**
     * API 1: Lấy danh sách đơn hàng
     * GET /api/orders
     * @return Danh sách đơn hàng mẫu (JSON)
     */
    @GetMapping
    public List<Map<String, Object>> getOrders() {
        return orders;
    }

    /**
     * API 2: Gọi Product Service để lấy thông tin sản phẩm theo ID
     * GET /api/orders/product/{id}
     * - Client gọi đến Order Service (cổng 8082)
     * - Order Service dùng RestClient gọi tiếp sang Product Service (cổng 8081)
     * - Xử lý đầy đủ: giữ nguyên mã HTTP 404 nếu không tìm thấy sản phẩm,
     *   báo lỗi HTTP 503 khi Product Service không hoạt động.
     * @param id Mã sản phẩm cần tra cứu
     * @return Dữ liệu sản phẩm nhận từ Product Service hoặc thông báo lỗi phù hợp
     */
    @GetMapping("/product/{id}")
    public ResponseEntity<String> getProduct(@PathVariable int id) {
        try {
            // Sử dụng RestClient thực hiện request GET /api/products/{id}
            String response = restClient.get()
                    .uri("/api/products/{id}", id)
                    .retrieve()
                    .body(String.class);

            // Trả về dữ liệu JSON nhận được cùng mã HTTP 200 OK
            return ResponseEntity.ok(response);

        } catch (HttpClientErrorException.NotFound e) {
            // Giữ nguyên mã HTTP 404 khi sản phẩm không tồn tại trong Product Service
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body("{\"message\": \"Không tìm thấy sản phẩm\"}");

        } catch (ResourceAccessException e) {
            // Xử lý khi Product Service (8081) bị tắt hoặc không thể kết nối
            return ResponseEntity.status(HttpStatus.SERVICE_UNAVAILABLE)
                    .body("{\"message\": \"Lỗi: Không thể kết nối đến Product Service (Service có thể chưa khởi động)\"}");

        } catch (Exception e) {
            // Xử lý các lỗi ngoại lệ khác
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("{\"message\": \"Lỗi hệ thống: " + e.getMessage() + "\"}");
        }
    }
}
