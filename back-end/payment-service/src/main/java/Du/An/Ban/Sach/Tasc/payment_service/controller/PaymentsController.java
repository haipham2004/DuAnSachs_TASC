package Du.An.Ban.Sach.Tasc.payment_service.controller;

import Du.An.Ban.Sach.Tasc.payment_service.client.ApiNotificationsClient;
import Du.An.Ban.Sach.Tasc.payment_service.client.ApiOrdersClient;
import Du.An.Ban.Sach.Tasc.payment_service.config.VNPayConfig;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.PaymentsRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentsResponse;
import Du.An.Ban.Sach.Tasc.payment_service.exception.CustomException;
import Du.An.Ban.Sach.Tasc.payment_service.repository.PaymentsRepository;
import Du.An.Ban.Sach.Tasc.payment_service.service.PaymentsService;
import Du.An.Ban.Sach.Tasc.payment_service.service.TransactionHistoryService;
import Du.An.Ban.Sach.Tasc.payment_service.service.VNPayService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("payments")
public class PaymentsController {

    private PaymentsService paymentsService;

    private VNPayService vnPayService;

    private ApiOrdersClient apiOrdersClient;

    private TransactionHistoryService transactionHistoryService;

    private ApiNotificationsClient apiNotificationsClient;

    private PaymentsRepository paymentsRepository;

    @Autowired
    public PaymentsController(PaymentsService paymentsService, VNPayService vnPayService, ApiOrdersClient apiOrdersClient,
                              TransactionHistoryService transactionHistoryService, ApiNotificationsClient apiNotificationsClient, PaymentsRepository paymentsRepository) {
        this.paymentsService = paymentsService;
        this.vnPayService = vnPayService;
        this.apiOrdersClient = apiOrdersClient;
        this.transactionHistoryService = transactionHistoryService;
        this.apiNotificationsClient = apiNotificationsClient;
        this.paymentsRepository = paymentsRepository;
    }

    @GetMapping("findAll")
    public ApiResponse<List<PaymentsResponse>> getAllPayments() {
        return ApiResponse.<List<PaymentsResponse>>builder().
                statusCode(200).message("findAll payments success").data(paymentsService.findAll()).build();  // Trả về HTTP 200 kèm dữ liệu
    }

    @GetMapping("findById/{id}")
    public ApiResponse<PaymentsResponse> findById(@PathVariable("id") Integer id) {
        return ApiResponse.<PaymentsResponse>builder().
                statusCode(200).message("findbyid payments success").data(paymentsService.findById(id)).build();  // Trả về HTTP 200 kèm dữ liệu
    }

    @PostMapping("save")
    public ApiResponse<PaymentsRequest> savePayment(@RequestBody PaymentsRequest paymentsRequest) {
        return ApiResponse.<PaymentsRequest>builder().statusCode(201).message("Save paymentss success")
                .data(paymentsService.save(paymentsRequest)).build();
    }

    @PutMapping("update/{id}")
    public ApiResponse<PaymentsRequest> update(@PathVariable("id") Integer id, @RequestBody PaymentsRequest paymentsRequest) {
        return ApiResponse.<PaymentsRequest>builder().statusCode(201).message("Update paymentss success")
                .data(paymentsService.update(id, paymentsRequest)).build();
    }

    @PostMapping("processPayment")
    public String processPayment(@RequestParam("amount") long orderTotal,
                                 @RequestParam("orderInfo") String orderInfo) {
        // Kiểm tra các tham số đầu vào
        if (orderTotal <= 0) {
            return "redirect:/errorPage";  // Ví dụ, nếu số tiền không hợp lệ
        }
        if (orderInfo == null || orderInfo.trim().isEmpty()) {
            return "redirect:/errorPage";  // Ví dụ, nếu thông tin đơn hàng thiếu
        }

        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo);
        return "redirect:" + vnpayUrl;
    }

    @RequestMapping("vnpay-payment-return")
    public String handleVNPayCallback(@RequestParam Map<String, String> params, Model model) {
        try {
            // Kiểm tra các tham số cần thiết
            if (params.isEmpty() || !params.containsKey("vnp_TxnRef") || !params.containsKey("vnp_ResponseCode")) {
                return "redirect:/payment-error";
            }

            // Kiểm tra kết quả thanh toán từ VNPay
            int status = vnPayService.orderReturn(params);
            Integer orderId = Integer.parseInt(params.get("vnp_OrderInfo"));
            String totalPayment = params.get("vnp_Amount");

            if (status == 1) {  // Thanh toán thành công
                // Lấy thông tin đơn hàng từ API
                ApiResponse<OrdersResponse> orderResponse = apiOrdersClient.findById(orderId);
                OrdersResponse ordersResponse = orderResponse.getData();

                if (ordersResponse == null) {
                    return "redirect:/payment-error";
                }


                model.addAttribute("orderId", ordersResponse.getOrderId());
                model.addAttribute("status", ordersResponse.getStatus());
                model.addAttribute("total", ordersResponse.getTotal());
                model.addAttribute("totalPayment", totalPayment);

//                       apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.SUCCESS.name());
//          Lưu lịch sử giao dịch
                TransactionHistoryRequest transactionHistoryRequest = TransactionHistoryRequest.builder()
                        .orderId(orderId)
                        .userId(ordersResponse.getUserId())
                        .status("SUCCESS_History")
                        .build();
                transactionHistoryService.save(transactionHistoryRequest);

                // Gửi thông báo email cho người dùng
                apiNotificationsClient.sendEmail(ordersResponse.getEmailUser(), "Xin chào Khách Hàng", ordersResponse.getOrderId());


                // Trả về view thành công
                return "PaymentSuccess";
            } else if (status == 0) {  // Thanh toán thất bại
                return "redirect:/payment-fail?orderId=" + orderId;
            } else {  // Lỗi hoặc dữ liệu không hợp lệ
                return "redirect:/payment-error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/payment-error";  // Redirect về trang lỗi nếu có ngoại lệ
        }
    }


//    @PostMapping("/refund")
//    public ResponseEntity<?> processRefund(
//            @RequestParam("transactionNo") String transactionNo,
//            @RequestParam("amount") long amount,
//            @RequestParam("description") String description) {
//
//        // In các tham số nhận được để kiểm tra
//        System.out.println("Transaction No: " + transactionNo);
//        System.out.println("Amount: " + amount);
//        System.out.println("Description: " + description);
//
//        if (amount <= 0) {
//            return ResponseEntity.badRequest().body("Invalid refund amount");
//        }
//
//        // Tạo hash
//        Map<String, String> params = new HashMap<>();
//        params.put("vnp_Command", "refund");
//        params.put("vnp_TransactionNo", transactionNo);
//        params.put("vnp_Amount", String.valueOf(amount * 100));
//        params.put("vnp_OrderInfo", description);
//        String hashData = VNPayConfig.hashAllFields(params);
//
//        // Gọi phương thức refundTransaction
//        String response = vnPayService.refundTransaction(transactionNo, amount, description, hashData);
//
//        if (response.contains("00")) {
//            return ResponseEntity.ok("Refund processed successfully");
//        } else {
//            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refund failed: " + response);
//        }
//    }


}
