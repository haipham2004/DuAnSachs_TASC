package com.example.orders_service.dto.response;

public enum PaymentStatus {
    PENDING,          // Chờ thanh toán
    COMPLETED,        // Đã thanh toán thành công
    FAILED,           // Thanh toán thất bại
    CANCELED,         // Thanh toán bị hủy
    REFUNDED,         // Đã hoàn tiền
    PARTIALLY_PAID,   // Thanh toán một phần
    UNDER_REVIEW,     // Đang được xem xét (thường gặp trong các giao dịch cần xác thực)
    EXPIRED           // Thanh toán hết hạn
}
