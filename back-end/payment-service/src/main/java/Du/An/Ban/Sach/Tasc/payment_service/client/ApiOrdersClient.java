package Du.An.Ban.Sach.Tasc.payment_service.client;

import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrderStatus;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="ORDERS-SERVICE")
public interface ApiOrdersClient {


    @GetMapping("/orders/findAll")
    ApiResponse<Page<OrdersResponse>> findAll(
            @RequestParam(defaultValue = "1") int pageNumber,
            @RequestParam(defaultValue = "5") int pageSize,
            @RequestParam(name = "fullName", required = false) String fullName,
            @RequestParam(name = "phone", required = false) String phone);

    @GetMapping("/orders/findById/{id}")
     ApiResponse<OrdersResponse> findById(@PathVariable("id") Integer id);

//    @GetMapping("/orders/findByIdOrder/{id}")
//    ApiResponse<OrdersResponse> findByIdOrder(@PathVariable("id") Integer id);

    @GetMapping("/orders/findByIdOrder/{id}")
    ApiResponse<OrdersResponse> findByIdOrder(@PathVariable("id") Integer id);

    @PutMapping("/orders/updateOrdersStatus")
    ApiResponse<Void> updateOrdersStatus( @RequestParam("idOrder") Integer idOrder,@RequestParam("status") OrderStatus status);

}
