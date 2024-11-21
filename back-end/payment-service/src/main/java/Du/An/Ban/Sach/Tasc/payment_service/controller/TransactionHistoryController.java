package Du.An.Ban.Sach.Tasc.payment_service.controller;

import Du.An.Ban.Sach.Tasc.payment_service.dto.request.PaymentsRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.service.TransactionHistoryService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("transactionhistory")
public class TransactionHistoryController {

    private TransactionHistoryService transactionHistoryService;

    @Autowired
    public TransactionHistoryController(TransactionHistoryService transactionHistoryService) {
        this.transactionHistoryService = transactionHistoryService;
    }

    @PostMapping("save")
    public ApiResponse<TransactionHistoryRequest> saveHistory(@RequestBody TransactionHistoryRequest transactionHistoryRequest){
        return ApiResponse.<TransactionHistoryRequest>builder().statusCode(201).message("Save Transactionhistory success")
                .data(transactionHistoryService.save(transactionHistoryRequest)).build();
    }
}
