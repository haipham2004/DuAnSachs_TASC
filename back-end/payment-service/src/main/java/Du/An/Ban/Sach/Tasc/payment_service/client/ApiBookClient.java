package Du.An.Ban.Sach.Tasc.payment_service.client;

import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.BooksResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("BOOKS-SERVICE")
public interface ApiBookClient {

    @PutMapping("reduceQuantitys")
    ApiResponse<List<BooksResponse>> reduceQuantitys(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                                            @RequestParam(name="quantity",defaultValue = "0") Integer quantity);



    @PutMapping("increaseQuantitys")
    ApiResponse<List<BooksResponse>>  increaseQuantitys(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                                               @RequestParam(name="quantity",defaultValue = "0") Integer quantity);
}
