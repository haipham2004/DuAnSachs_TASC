package Du.An.Ban.Sach.Tasc.payment_service.dto.response;

public enum OrderStatus {
    NEW,               // Đơn hàng mới, chưa được xử lý
    PENDING,           // Đơn hàng đang chờ xử lý (ví dụ: chờ thanh toán, kiểm tra kho)
    CONFIRMED,         // Đơn hàng đã được xác nhận (thanh toán thành công, kiểm tra kho ok)
    PROCESSING,        // Đơn hàng đang được xử lý (gói hàng, chuẩn bị vận chuyển)
    SHIPPED,           // Đơn hàng đã được gửi đi (giao cho dịch vụ vận chuyển)
    DELIVERED,         // Đơn hàng đã được giao thành công
    CANCELLED,         // Đơn hàng đã bị hủy
    RETURNED,
    SUCCESS  // Đơn hàng đã được trả lại
}
