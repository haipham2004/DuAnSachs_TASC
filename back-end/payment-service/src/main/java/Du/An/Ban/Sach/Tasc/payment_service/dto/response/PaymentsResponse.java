package Du.An.Ban.Sach.Tasc.payment_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

import java.sql.Date;
import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
@Builder
public class PaymentsResponse {

    private Integer paymentId;

    private LocalDateTime paymentDate;

    private Double amount;

    private String paymentMethod;

    private String status;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private boolean deletedAt;

    private Integer idOrder;

    private Double total;

    private String statusOrder;

    private String fullNameOrder;

}
