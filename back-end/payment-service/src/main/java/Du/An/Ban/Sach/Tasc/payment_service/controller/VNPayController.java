package Du.An.Ban.Sach.Tasc.payment_service.controller;

import Du.An.Ban.Sach.Tasc.payment_service.client.ApiNotificationsClient;
import Du.An.Ban.Sach.Tasc.payment_service.client.ApiOrdersClient;
import Du.An.Ban.Sach.Tasc.payment_service.config.VNPayConfig;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrderStatus;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentStatus;
import Du.An.Ban.Sach.Tasc.payment_service.entity.Payments;
import Du.An.Ban.Sach.Tasc.payment_service.repository.PaymentsRepository;
import Du.An.Ban.Sach.Tasc.payment_service.service.TransactionHistoryService;
import Du.An.Ban.Sach.Tasc.payment_service.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class VNPayController {


    private VNPayService vnPayService;

    private PaymentsRepository paymentsRepository;

    private ApiOrdersClient apiOrdersClient;

    private ApiNotificationsClient apiNotificationsClient;

    private TransactionHistoryService transactionHistoryService;

@Autowired
    public VNPayController(VNPayService vnPayService, PaymentsRepository paymentsRepository,
                           ApiOrdersClient apiOrdersClient, ApiNotificationsClient apiNotificationsClient, TransactionHistoryService transactionHistoryService) {
        this.vnPayService = vnPayService;
        this.paymentsRepository = paymentsRepository;
        this.apiOrdersClient = apiOrdersClient;
        this.apiNotificationsClient = apiNotificationsClient;
        this.transactionHistoryService = transactionHistoryService;
    }

    @GetMapping({"", "/"})
    public String home() {
        return "createOrder";
    }

    // Chuyển hướng người dùng đến cổng thanh toán VNPAY
    @PostMapping("/submitOrder")
    public String submidOrder(@RequestParam("amount") long orderTotal,
                              @RequestParam("orderInfo") String orderInfo,
                              HttpServletRequest request) {
        // Kiểm tra các tham số đầu vào
        if (orderTotal <= 0) {
            return "redirect:/errorPage";  // Ví dụ, nếu số tiền không hợp lệ
        }
        if (orderInfo == null || orderInfo.trim().isEmpty()) {
            return "redirect:/errorPage";  // Ví dụ, nếu thông tin đơn hàng thiếu
        }

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String vnpayUrl = vnPayService.createOrder(request, orderTotal, orderInfo, baseUrl);
        return "redirect:" + vnpayUrl;
    }

    // Sau khi hoàn tất thanh toán, VNPAY sẽ chuyển hướng trình duyệt về URL này
    @GetMapping("/vnpay-payment-return")
    public String paymentCompleted(HttpServletRequest request, Model model){
        int paymentStatus =vnPayService.orderReturn(request);
        // Kiểm tra nếu giao dịch thành công
        if (paymentStatus == 1) {
//            String orderInfo = request.getParameter("vnp_OrderInfo");
//            String[] parts = orderInfo.split("\\+");
//            Integer orderId = Integer.parseInt(parts[parts.length - 1]);
            Integer orderId = Integer.parseInt(request.getParameter("vnp_OrderInfo"));
            String transactionNo = request.getParameter("vnp_TransactionNo");
            double amount = Double.parseDouble(request.getParameter("vnp_Amount")) / 100; // Chuyển sang VNĐ
            String description = "Thanh toán đơn hàng #" + orderId;

            ApiResponse<OrdersResponse> orderResponseApi = apiOrdersClient.findById(orderId);

            OrdersResponse order = orderResponseApi.getData();
            // Tạo đối tượng Payments
            Payments payments = new Payments();
            payments.setIdOrder(orderId);
            payments.setAmount(amount);
            payments.setPaymentMethod("VNPAY");
            payments.setDeletedAt(false);
            payments.setPaymentDate(LocalDateTime.now());
            payments.setStatus(PaymentStatus.SUCCESS.name());

            // Lưu thông tin thanh toán vào cơ sở dữ liệu
            Payments savedPayment = paymentsRepository.save(payments);

            // Cập nhật trạng thái đơn hàng thành công
            apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.SUCCESS.name());

            // Lưu lịch sử giao dịch
            TransactionHistoryRequest transactionHistoryRequest = TransactionHistoryRequest.builder()
                    .orderId(orderId)
                    .userId(order.getUserId())
                    .status("SUCCESS_History")
                    .build();
            transactionHistoryService.save(transactionHistoryRequest);

            // Gửi thông báo email cho người dùng
            apiNotificationsClient.sendEmail(order.getEmailUser(),"Xin chào Khách Hàng",order.getOrderId());
            // Trả về thông báo thành công
            return "Payment processed successfully with ID: " + savedPayment.getPaymentId();
        } else {
            return "Payment failed, status code: " + paymentStatus;
        }
    }

    @PostMapping("/refund")
    public ResponseEntity<?> processRefund(
            @RequestParam("transactionNo") String transactionNo,
            @RequestParam("amount") long amount,
            @RequestParam("description") String description) {

        // In các tham số nhận được để kiểm tra
        System.out.println("Transaction No: " + transactionNo);
        System.out.println("Amount: " + amount);
        System.out.println("Description: " + description);

        if (amount <= 0) {
            return ResponseEntity.badRequest().body("Invalid refund amount");
        }

        // Tạo hash
        Map<String, String> params = new HashMap<>();
        params.put("vnp_Command", "refund");
        params.put("vnp_TransactionNo", transactionNo);
        params.put("vnp_Amount", String.valueOf(amount * 100));
        params.put("vnp_OrderInfo", description);
        String hashData = VNPayConfig.hashAllFields(params);

        // Gọi phương thức refundTransaction
        String response = vnPayService.refundTransaction(transactionNo, amount, description, hashData);

        if (response.contains("00")) {
            return ResponseEntity.ok("Refund processed successfully");
        } else {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body("Refund failed: " + response);
        }
    }




}
