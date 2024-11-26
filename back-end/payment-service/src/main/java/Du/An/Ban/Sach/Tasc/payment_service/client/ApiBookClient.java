package Du.An.Ban.Sach.Tasc.payment_service.client;

import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.BooksResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient("BOOKS-SERVICE")
public interface ApiBookClient {

    @PutMapping("/books/reduceQuantitys")
    ApiResponse<List<BooksResponse>> reduceQuantitys(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
                                                            @RequestParam(name="quantity",defaultValue = "0") Integer quantity);



//    @PutMapping("/books/increaseQuantitys")
//    ApiResponse<List<BooksResponse>>  increaseQuantitys(@RequestParam(name="bookId",defaultValue = "0") Integer bookId,
//                                                               @RequestParam(name="quantity",defaultValue = "0") Integer quantity);
//
//
//    @PutMapping("/books/decreaseQuantity")
//    ApiResponse<Void> decreaseQuantity(@RequestParam(name = "bookId", defaultValue = "0") Integer bookId,
//                                              @RequestParam(name = "quantity", defaultValue = "0") Integer quantity);
}
