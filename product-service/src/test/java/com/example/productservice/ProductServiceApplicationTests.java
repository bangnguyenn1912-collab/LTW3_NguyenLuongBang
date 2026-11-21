package com.example.productservice;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * Lớp kiểm thử cơ bản cho Product Service.
 * Xác nhận Spring Boot Application có thể khởi động và nạp Application Context thành công.
 */
@SpringBootTest
class ProductServiceApplicationTests {

    /**
     * Kiểm tra Application Context được nạp thành công khi khởi động dịch vụ.
     */
    @Test
    void contextLoads() {
        // Kiểm tra cơ bản: nếu Application Context không khởi động được, test case sẽ báo lỗi
    }

}
