package Du.An.Ban.Sach.Tasc.payment_service.controller;

import Du.An.Ban.Sach.Tasc.payment_service.config.VNPayConfig;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.RefundRequest;
import Du.An.Ban.Sach.Tasc.payment_service.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/v1/payment")
public class PaymentController {


    private VNPayService vnPayService;

    @Autowired
    public PaymentController(VNPayService vnPayService) {
        this.vnPayService = vnPayService;
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

        String orderInfo = request.getParameter("vnp_OrderInfo");
        String paymentTime = request.getParameter("vnp_PayDate");
        String transactionId = request.getParameter("vnp_TransactionNo");
        String totalPrice = request.getParameter("vnp_Amount");

        model.addAttribute("orderId", orderInfo);
        model.addAttribute("totalPrice", totalPrice);
        model.addAttribute("paymentTime", paymentTime);
        model.addAttribute("transactionId", transactionId);

        return paymentStatus == 1 ? "ordersuccess" : "orderfail";
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
