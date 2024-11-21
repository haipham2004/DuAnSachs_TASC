package Du.An.Ban.Sach.Tasc.payment_service.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@JsonInclude(JsonInclude.Include.NON_NULL)
public class BooksResponse {

    private Integer bookId;

    private String title;

    private String nameAuthor;

    private String namePublisher;

    private String nameCategory;

    private Double price;

    private Double consPrice;

    private String description;

    private Integer quantity;

    private String status;

    private List<String> imageUrl;

    private String thumbnail;

    private Integer authorId;

    private Integer publisherId;

    private Integer categoryId;
}
