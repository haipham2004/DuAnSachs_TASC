package Du.An.Ban.Sach.Tasc.payment_service.controller;

import Du.An.Ban.Sach.Tasc.payment_service.dto.request.PaymentsRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentsResponse;
import Du.An.Ban.Sach.Tasc.payment_service.service.PaymentsService;
import Du.An.Ban.Sach.Tasc.payment_service.service.VNPayService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("payments")
@Slf4j
public class PaymentsController {

    private PaymentsService paymentsService;

    private VNPayService vnPayService;



//    private  KafkaTemplate<String, Object> kafkaTemplate;


    @Autowired
    public PaymentsController(PaymentsService paymentsService, VNPayService vnPayService) {
        this.paymentsService = paymentsService;
        this.vnPayService = vnPayService;
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
            return "redirect:/errorPage";
        }
        if (orderInfo == null || orderInfo.trim().isEmpty()) {
            return "redirect:/errorPage";
        }

        String vnpayUrl = vnPayService.createOrder(orderTotal, orderInfo);
        return "redirect:" + vnpayUrl;
    }

    @RequestMapping("vnpay-payment-return")
    public String handleVNPayCallback(@RequestParam Map<String, String> params, Model model) {
        try {
            if (params.isEmpty() || !params.containsKey("vnp_TxnRef") || !params.containsKey("vnp_ResponseCode")) {
                return "redirect:/payment-error";
            }

            int status = vnPayService.processVNPayCallback(params);
            Integer orderId = Integer.parseInt(params.get("vnp_OrderInfo"));

            // If status is successful
            if (status == 1) {
                return "redirect:/PaymentSuccess";
            } else if (status == 0) {
                return "redirect:/payment-fail?orderId=" + orderId;
            } else {
                return "redirect:/payment-error";
            }
        } catch (Exception e) {
            e.printStackTrace();
            return "redirect:/payment-error";
        }
    }
}


//    @RequestMapping("vnpay-payment-return")
//    public String handleVNPayCallback(@RequestParam Map<String, String> params, Model model) {
//        try {
//            if (params.isEmpty() || !params.containsKey("vnp_TxnRef") || !params.containsKey("vnp_ResponseCode")) {
//                return "redirect:/payment-error";
//            }
//
//            int status = vnPayService.orderReturn(params);
//            Integer orderId = Integer.parseInt(params.get("vnp_OrderInfo"));
//            String totalPayment = params.get("vnp_Amount");
//
//            if (status == 1) {
//
//                ApiResponse<OrdersResponse> orderResponse = apiOrdersClient.findByIdOrder(orderId);
//                OrdersResponse ordersResponse = orderResponse.getData();
//
//                log.info("Log 1: "+ordersResponse.getOrdersItems());
//                log.info("Log 2: "+ordersResponse);
//
//                for (OrdersItemsResponse item : ordersResponse.getOrdersItems()) {
//                    Integer bookId = item.getBookId();
//                    Integer quantity = item.getQuantity();
//                    ApiResponse<List<BooksResponse>> bookResponse = apiBookClient.reduceQuantitys(bookId, quantity);
//                    if (bookResponse == null || bookResponse.getData() == null || bookResponse.getData().isEmpty()) {
//                        apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.CANCELLED.name());
//                        paymentsService.updatePaymentStatus(orderId, "FAILED");
//                        return "redirect:/payment-fail?orderId=" + orderId;
//                    }
//                }
//
//
//
//                apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.SUCCESS.name());
//                paymentsService.updatePaymentStatus(orderId, "SUCCESS");
//                TransactionHistoryRequest transactionHistoryRequest = TransactionHistoryRequest.builder()
//                        .orderId(orderId)
//                        .userId(ordersResponse.getUserId())
//                        .status("SUCCESS_History")
//                        .build();
//                transactionHistoryService.save(transactionHistoryRequest);
//
//
////                MessageDto messageDto = new MessageDto(
////                        orderId, // ID đơn hàng
////                        ordersResponse.getEmailUser(), // Địa chỉ email
////                        "Thông báo thanh toán thành công", // Tiêu đề
////                        "Cảm ơn bạn đã thanh toán, đơn hàng của bạn đã được xử lý thành công.", // Nội dung
////                        "Thanh toán thành công cho đơn hàng #" + orderId // Nội dung ngắn gọn
////                );
//
//
////                kafkaTemplate.send("notification-events", messageDto);
//                apiNotificationsClient.sendEmail(ordersResponse.getEmailUser(), "Đơn hàng đã được thanh toán thành công", ordersResponse.getOrderId());
//                return "redirect:/PaymentSuccess";
//            } else if (status == 0) {
//                apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.CANCELLED.name());
//                paymentsService.updatePaymentStatus(orderId, "FAILED");
//                return "redirect:/payment-fail?orderId=" + orderId;
//            } else {
//                return "redirect:/payment-error";
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return "redirect:/payment-error";
//        }
//    }
//}


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

