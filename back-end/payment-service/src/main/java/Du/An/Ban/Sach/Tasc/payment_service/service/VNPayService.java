package Du.An.Ban.Sach.Tasc.payment_service.service;

import Du.An.Ban.Sach.Tasc.payment_service.client.ApiBookClient;
import Du.An.Ban.Sach.Tasc.payment_service.client.ApiNotificationsClient;
import Du.An.Ban.Sach.Tasc.payment_service.client.ApiOrdersClient;
import Du.An.Ban.Sach.Tasc.payment_service.config.VNPayConfig;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.BooksResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrderStatus;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersItemsResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.TimeZone;

@Service
public class VNPayService {

    private PaymentsService paymentsService;


    private ApiOrdersClient apiOrdersClient;

    private TransactionHistoryService transactionHistoryService;

    private ApiNotificationsClient apiNotificationsClient;

    private ApiBookClient apiBookClient;

    @Autowired
    public VNPayService(PaymentsService paymentsService, ApiOrdersClient apiOrdersClient,
                        TransactionHistoryService transactionHistoryService, ApiNotificationsClient apiNotificationsClient, ApiBookClient apiBookClient) {
        this.paymentsService = paymentsService;
        this.apiOrdersClient = apiOrdersClient;
        this.transactionHistoryService = transactionHistoryService;
        this.apiNotificationsClient = apiNotificationsClient;
        this.apiBookClient = apiBookClient;
    }

    public String createOrder(long amount, String orderInfor){
        // Các bạn có thể tham khảo tài liệu hướng dẫn và điều chỉnh các tham số
        String vnp_Version = "2.1.0";
        String vnp_Command = "pay";
        String vnp_TxnRef = VNPayConfig.getRandomNumber(8);
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String orderType = "order-type";
        String vnp_IpAddr = "192.168.1.15";
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", vnp_Version);
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100));
        vnp_Params.put("vnp_CurrCode", "VND");

        vnp_Params.put("vnp_TxnRef", vnp_TxnRef);
        vnp_Params.put("vnp_OrderInfo", orderInfor);
        vnp_Params.put("vnp_OrderType", orderType);

        String locate = "vn";
        vnp_Params.put("vnp_Locale", locate);

       String urlReturn = VNPayConfig.vnp_Returnurl;
        vnp_Params.put("vnp_ReturnUrl", urlReturn);
        vnp_Params.put("vnp_IpAddr", vnp_IpAddr);  // Sử dụng IP truyền vào

        Calendar cld = Calendar.getInstance(TimeZone.getTimeZone("Etc/GMT+7"));
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);

        cld.add(Calendar.MINUTE, 15);
        String vnp_ExpireDate = formatter.format(cld.getTime());
        vnp_Params.put("vnp_ExpireDate", vnp_ExpireDate);

        List<String> fieldNames = new ArrayList<>(vnp_Params.keySet());
        Collections.sort(fieldNames);
        StringBuilder hashData = new StringBuilder();
        StringBuilder query = new StringBuilder();
        Iterator<String> itr = fieldNames.iterator();
        while (itr.hasNext()) {
            String fieldName = itr.next();
            String fieldValue = vnp_Params.get(fieldName);
            if ((fieldValue != null) && (fieldValue.length() > 0)) {
                // Build hash data
                hashData.append(fieldName);
                hashData.append('=');
                try {
                    hashData.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                    // Build query
                    query.append(URLEncoder.encode(fieldName, StandardCharsets.US_ASCII.toString()));
                    query.append('=');
                    query.append(URLEncoder.encode(fieldValue, StandardCharsets.US_ASCII.toString()));
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                if (itr.hasNext()) {
                    query.append('&');
                    hashData.append('&');
                }
            }
        }
        String queryUrl = query.toString();
        String salt = VNPayConfig.vnp_HashSecret;
        String vnp_SecureHash = VNPayConfig.hmacSHA512(salt, hashData.toString());
        queryUrl += "&vnp_SecureHash=" + vnp_SecureHash;
        String paymentUrl = VNPayConfig.vnp_PayUrl + "?" + queryUrl;
        return paymentUrl;
    }

    public int processVNPayCallback(Map<String, String> params) {
        try {
            // Tạo một map các tham số từ callback
            Map<String, String> fields = new HashMap<>();
            for (String key : params.keySet()) {
                String value = params.get(key);
                if (value != null && !value.isEmpty()) {
                    fields.put(key, value);
                }
            }

            // Lấy giá trị của SecureHash từ callback
            String vnp_SecureHash = params.get("vnp_SecureHash");

            // Loại bỏ các trường không cần thiết khỏi fields
            fields.remove("vnp_SecureHash");
            fields.remove("vnp_SecureHashType");

            // Tính toán chữ ký bảo mật từ các tham số còn lại
            String signValue = VNPayConfig.hashAllFields(fields);

            // Kiểm tra chữ ký và trạng thái thanh toán
            if (signValue.equals(vnp_SecureHash)) {
                Integer orderId = Integer.parseInt(params.get("vnp_OrderInfo"));
                String totalPayment = params.get("vnp_Amount");

                if ("00".equals(params.get("vnp_TransactionStatus"))) {
                    // Thanh toán thành công
                    return handleSuccessfulPayment(params, orderId, totalPayment);
                } else {
                    // Thanh toán thất bại
                    return handleFailedPayment(orderId);
                }
            } else {
                // Nếu chữ ký không khớp
                return -1; // Lỗi chữ ký
            }
        } catch (Exception e) {
            e.printStackTrace();
            return -1; // Lỗi bất kỳ
        }
    }

    private int handleSuccessfulPayment(Map<String, String> params, Integer orderId, String totalPayment) {
        ApiResponse<OrdersResponse> orderResponse = apiOrdersClient.findByIdOrder(orderId);
        OrdersResponse ordersResponse = orderResponse.getData();


        for (OrdersItemsResponse item : ordersResponse.getOrderItems()) {
            Integer bookId = item.getBookId();
            Integer quantity = item.getQuantity();
            ApiResponse<List<BooksResponse>> bookResponse = apiBookClient.reduceQuantitys(bookId, quantity);

            if (bookResponse == null || bookResponse.getData() == null || bookResponse.getData().isEmpty()) {
                apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.CANCELLED);
                paymentsService.updatePaymentStatus(orderId, "FAILED");
                return 0; // Thanh toán thất bại
            }
        }

        apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.SUCCESS);
        paymentsService.updatePaymentStatus(orderId, "SUCCESS");

        // Lưu lịch sử giao dịch
        TransactionHistoryRequest transactionHistoryRequest = TransactionHistoryRequest.builder()
                .orderId(orderId)
                .userId(ordersResponse.getUserId())
                .status("SUCCESS_History")
                .build();
        transactionHistoryService.save(transactionHistoryRequest);

        // Gửi email thông báo thanh toán thành công
        apiNotificationsClient.sendEmail(ordersResponse.getEmailUser(), "Đơn hàng đã được thanh toán thành công", ordersResponse.getOrderId());

        return 1; // Thành công
    }

    private int handleFailedPayment(Integer orderId) {
        apiOrdersClient.updateOrdersStatus(orderId, OrderStatus.CANCELLED);
        paymentsService.updatePaymentStatus(orderId, "FAILED");
        return 0; // Thanh toán thất bại
    }


//    public int orderReturn(Map<String, String> params) {
//        try {
//            // Tạo một map các tham số từ callback
//            Map<String, String> fields = new HashMap<>();
//            for (String key : params.keySet()) {
//                String value = params.get(key);
//                if (value != null && !value.isEmpty()) {
//                    fields.put(key, value);
//                }
//            }
//
//            // Lấy giá trị của SecureHash từ callback
//            String vnp_SecureHash = params.get("vnp_SecureHash");
//
//            // Loại bỏ các trường không cần thiết khỏi fields
//            fields.remove("vnp_SecureHash");
//            fields.remove("vnp_SecureHashType");
//
//            // Tính toán chữ ký bảo mật từ các tham số còn lại
//            String signValue = VNPayConfig.hashAllFields(fields);
//
//            // Kiểm tra chữ ký và trạng thái thanh toán
//            if (signValue.equals(vnp_SecureHash)) {
//                if ("00".equals(params.get("vnp_TransactionStatus"))) {
//                    // Thanh toán thành công
//                    return 1; // Thành công
//                } else {
//                    // Thanh toán thất bại
//                    return 0; // Thất bại
//                }
//            } else {
//                // Nếu chữ ký không khớp
//                return -1; // Lỗi chữ ký
//            }
//        } catch (Exception e) {
//            e.printStackTrace();
//            return -1; // Lỗi bất kỳ
//        }
//    }

    public String refundTransaction(String transactionNo, double amount, String createdBy, String description) {
        String vnp_Command = "refund";
        String vnp_TmnCode = VNPayConfig.vnp_TmnCode;
        String vnp_HashSecret = VNPayConfig.vnp_HashSecret;

        // Tạo danh sách các tham số
        Map<String, String> vnp_Params = new HashMap<>();
        vnp_Params.put("vnp_Version", "2.1.0");
        vnp_Params.put("vnp_Command", vnp_Command);
        vnp_Params.put("vnp_TmnCode", vnp_TmnCode);
        vnp_Params.put("vnp_TransactionNo", transactionNo);
        vnp_Params.put("vnp_Amount", String.valueOf(amount * 100)); // Đơn vị: VNĐ x 100
        vnp_Params.put("vnp_TransactionType", "03"); // Loại giao dịch hoàn tiền
        vnp_Params.put("vnp_CreateBy", createdBy);

        // Định dạng ngày tháng
        SimpleDateFormat formatter = new SimpleDateFormat("yyyyMMddHHmmss");
        String vnp_CreateDate = formatter.format(new Date());
        vnp_Params.put("vnp_CreateDate", vnp_CreateDate);
        vnp_Params.put("vnp_Description", description);

        // Tạo hash dữ liệu để bảo mật
        String hashData = VNPayConfig.hashAllFields(vnp_Params); // Tạo chuỗi dữ liệu hash
        String vnp_SecureHash = VNPayConfig.hmacSHA512(vnp_HashSecret, hashData); // Tạo chữ ký bảo mật
        vnp_Params.put("vnp_SecureHash", vnp_SecureHash);

        // Tạo URL gửi yêu cầu
        String queryUrl = VNPayConfig.vnp_apiUrl;

        try {
            // Tạo kết nối HTTP
            URL url = new URL(queryUrl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();
            conn.setRequestMethod("POST");
            conn.setDoOutput(true);

            // Tạo nội dung gửi trong POST request
            StringBuilder postData = new StringBuilder();
            for (Map.Entry<String, String> entry : vnp_Params.entrySet()) {
                if (postData.length() > 0) postData.append("&");
                postData.append(URLEncoder.encode(entry.getKey(), "UTF-8"));
                postData.append("=");
                postData.append(URLEncoder.encode(entry.getValue(), "UTF-8"));
            }

            // Gửi dữ liệu POST
            conn.getOutputStream().write(postData.toString().getBytes("UTF-8"));

            // Đọc phản hồi từ VNPay
            BufferedReader in = new BufferedReader(new InputStreamReader(conn.getInputStream()));
            String inputLine;
            StringBuilder response = new StringBuilder();

            while ((inputLine = in.readLine()) != null) {
                response.append(inputLine);
            }
            in.close();

            // Trả về phản hồi
            return response.toString();
        } catch (Exception e) {
            e.printStackTrace();
            return "Error: " + e.getMessage();
        }
    }
}
