package Du.An.Ban.Sach.Tasc.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
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
@JsonInclude(JsonInclude.Include.NON_NULL)
public class OrdersItemsResponse {

    private int orderItemId;

    private int quantity;

    private Double price;

    private String status;

    @JsonIgnore
    private LocalDateTime createdAt;
    @JsonIgnore
    private LocalDateTime updatedAt;
    @JsonIgnore
    private Boolean deletedAt;

    private int orderId;

    private int bookId;

    private String tileBook;

    private Double total;

}
