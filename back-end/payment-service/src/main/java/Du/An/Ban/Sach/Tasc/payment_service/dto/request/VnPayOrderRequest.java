package Du.An.Ban.Sach.Tasc.payment_service.dto.request;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class VnPayOrderRequest {
    private Integer orderId;         // ID của đơn hàng
    private double amount;          // Số tiền thanh toán (đơn vị là VNĐ)
    private String orderInfo;       // Thông tin về đơn hàng (ví dụ: mô tả sản phẩm)
    private String returnUrl;       // URL sẽ nhận phản hồi (callback) từ VNPay sau khi thanh toán
    private String paymentMethod;
}
