package Du.An.Ban.Sach.Tasc.payment_service.controller;

import Du.An.Ban.Sach.Tasc.payment_service.dto.request.PaymentsRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentsResponse;
import Du.An.Ban.Sach.Tasc.payment_service.service.PaymentsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    private PaymentsService paymentsService;

    @Autowired
    public PaymentsController(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
    }

    @GetMapping("findAll")
    public ApiResponse<List<PaymentsResponse>> getAllPayments() {
        return ApiResponse.< List<PaymentsResponse>>builder().
                statusCode(200).message("findAll payments success").data(paymentsService.findAll()).build();  // Trả về HTTP 200 kèm dữ liệu
    }

    @GetMapping("findById/{id}")
    public ApiResponse<PaymentsResponse> findById(@PathVariable("id") Integer id)  {
        return ApiResponse.< PaymentsResponse>builder().
                statusCode(200).message("findbyid payments success").data(paymentsService.findById(id)).build();  // Trả về HTTP 200 kèm dữ liệu
    }

    @PostMapping("save")
    public ApiResponse<PaymentsRequest> save(@RequestBody PaymentsRequest paymentsRequest){
        return ApiResponse.<PaymentsRequest>builder().statusCode(201).message("Save paymentss success")
                .data(paymentsService.save(paymentsRequest)).build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<PaymentsRequest> update(@PathVariable("id") Integer id, @RequestBody PaymentsRequest paymentsRequest){
        return ApiResponse.<PaymentsRequest>builder().statusCode(201).message("Update paymentss success")
                .data(paymentsService.update(id,paymentsRequest)).build();
    }

    @PostMapping("processPayment")
    public ApiResponse<String> processPayment(@RequestBody PaymentsRequest paymentsRequest){
        return ApiResponse.<String>builder().statusCode(201).message("Thanh toán paymentss success")
                .data(paymentsService.processPayment(paymentsRequest)).build();
    }

}
