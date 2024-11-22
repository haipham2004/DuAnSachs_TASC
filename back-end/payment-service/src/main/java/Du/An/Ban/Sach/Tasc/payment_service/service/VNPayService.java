package Du.An.Ban.Sach.Tasc.payment_service.service;

import Du.An.Ban.Sach.Tasc.payment_service.config.VNPayConfig;
import org.springframework.stereotype.Service;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class VNPayService {
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

    public int orderReturn(Map<String, String> params) {
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
                if ("00".equals(params.get("vnp_TransactionStatus"))) {
                    // Thanh toán thành công
                    return 1; // Thành công
                } else {
                    // Thanh toán thất bại
                    return 0; // Thất bại
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
