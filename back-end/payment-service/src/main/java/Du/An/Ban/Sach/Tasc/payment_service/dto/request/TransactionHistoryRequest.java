package Du.An.Ban.Sach.Tasc.payment_service.dto.request;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class TransactionHistoryRequest {

    private Integer transactionId;

    private Integer orderId;

    private Integer userId;

    private LocalDateTime transactionDate;

    private String status;

}
