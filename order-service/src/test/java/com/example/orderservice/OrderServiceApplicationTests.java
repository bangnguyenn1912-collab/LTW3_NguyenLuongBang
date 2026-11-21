package com.example.orderservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Lớp kiểm thử cơ bản cho Order Service.
 * Xác nhận Spring Boot Application có thể khởi động và nạp Application Context thành công.
 */
@SpringBootTest
class OrderServiceApplicationTests {

    /**
     * Kiểm tra Application Context được nạp thành công khi khởi động dịch vụ.
     */
    @Test
    void contextLoads() {
        // Kiểm tra cơ bản: nếu Application Context không khởi động được, test case sẽ báo lỗi
    }

}
