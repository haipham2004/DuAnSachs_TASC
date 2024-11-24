package com.example.common_service.dto.response;

public enum OrderStatus {
    CREATED,      // Đơn hàng được tạo
    PROCESSING,   // Đang xử lý
    CANCELLED,    // Bị hủy
    COMPLETED         // Đơn hàng đã được trả lại
}
