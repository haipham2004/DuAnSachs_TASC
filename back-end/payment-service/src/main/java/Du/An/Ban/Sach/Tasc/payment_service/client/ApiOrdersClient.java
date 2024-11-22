package Du.An.Ban.Sach.Tasc.payment_service.client;

import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PageResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name="ORDERS-SERVICE")
public interface ApiOrdersClient {

    @GetMapping("/orders/findAll")
     ApiResponse<PageResponse<OrdersResponse>> findAll(@RequestParam(name = "fullname", defaultValue = "") String fullname,
                                                             @RequestParam(name = "phone", defaultValue = "") String phone,
                                                             @RequestParam(defaultValue = "1") int pageNumber,
                                                             @RequestParam(defaultValue = "5") int pageSize);

    @GetMapping("/orders/findById/{id}")
     ApiResponse<OrdersResponse> findById(@PathVariable("id") Integer id);

    @PutMapping("/orders/updateOrdersStatus")
    ApiResponse<Void> updateOrdersStatus( @RequestParam("idOrder") Integer idOrder,@RequestParam("status") String status);

}
