/**
 * Hàm gọi API lấy danh sách sản phẩm từ Product Service (Port 8081)
 * Tuân thủ chuẩn tài liệu Lab 01
 */
async function loadProducts() {
    try {
        const response = await fetch("http://localhost:8081/api/products");
        
        if (!response.ok) {
            throw new Error("Không thể tải danh sách sản phẩm");
        }
        
        const products = await response.json();
        const list = document.getElementById("productList");
        list.innerHTML = "";
        
        if (products.length === 0) {
            list.textContent = "Chưa có sản phẩm";
            return;
        }
        
        products.forEach(product => {
            const li = document.createElement("li");
            li.textContent = `${product.id} - ${product.name}`;
            list.appendChild(li);
        });
    } catch (error) {
        document.getElementById("productList").textContent = "Lỗi: " + error.message;
    }
}

/**
 * Hàm gọi API tìm kiếm sản phẩm thông qua Order Service (Port 8082)
 * Order Service sẽ dùng Spring RestClient để lấy thông tin từ Product Service (Port 8081)
 * Tuân thủ chuẩn tài liệu Lab 01
 */
async function findProduct() {
    const id = document.getElementById("productId").value;
    const result = document.getElementById("result");

    // Kiểm tra tính hợp lệ của ID: không để trống, phải là số nguyên, lớn hơn hoặc bằng 1
    if (!id || !Number.isInteger(Number(id)) || Number(id) < 1) {
        result.textContent = "Vui lòng nhập ID hợp lệ";
        return;
    }

    try {
        const response = await fetch(`http://localhost:8082/api/orders/product/${id}`);

        // Trường hợp Product Service trả về 404 (sản phẩm không tồn tại)
        if (response.status === 404) {
            result.textContent = "Không tìm thấy sản phẩm";
            return;
        }

        if (!response.ok) {
            throw new Error("Không thể lấy thông tin sản phẩm");
        }

        const product = await response.json();
        // Hiển thị định dạng JSON rõ ràng
        result.textContent = JSON.stringify(product, null, 2);
    } catch (error) {
        result.textContent = "Lỗi: " + error.message;
    }
}
