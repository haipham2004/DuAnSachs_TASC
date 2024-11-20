package Du.An.Ban.Sach.Tasc.payment_service.dto.response;

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
public  class VNPayResponse {
    public String code;
    public String message;
    public String paymentUrl;
}