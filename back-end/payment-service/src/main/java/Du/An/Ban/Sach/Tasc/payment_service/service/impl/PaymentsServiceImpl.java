package Du.An.Ban.Sach.Tasc.payment_service.service.impl;

import Du.An.Ban.Sach.Tasc.payment_service.client.ApiNotificationsClient;
import Du.An.Ban.Sach.Tasc.payment_service.client.ApiOrdersClient;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.PaymentsRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.request.VnPayOrderRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.ApiResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrderStatus;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.OrdersResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PageResponse;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentStatus;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentsMethod;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentsResponse;
import Du.An.Ban.Sach.Tasc.payment_service.entity.Payments;
import Du.An.Ban.Sach.Tasc.payment_service.exception.CustomException;
import Du.An.Ban.Sach.Tasc.payment_service.exception.MessageExceptionResponse;
import Du.An.Ban.Sach.Tasc.payment_service.exception.NotfoundException;
import Du.An.Ban.Sach.Tasc.payment_service.repository.PaymentsRepository;
import Du.An.Ban.Sach.Tasc.payment_service.service.PaymentsService;
import Du.An.Ban.Sach.Tasc.payment_service.service.TransactionHistoryService;
import Du.An.Ban.Sach.Tasc.payment_service.service.VNPayService;
import jakarta.servlet.http.HttpServletRequest;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Service
public class PaymentsServiceImpl implements PaymentsService {

    private PaymentsRepository paymentsRepository;

    private ModelMapper modelMapper;

    private ApiOrdersClient apiOrdersClient;

    private ApiNotificationsClient apiNotificationsClient;

    private TransactionHistoryService transactionHistoryService;

    private VNPayService vnPayService;

    @Autowired
    public PaymentsServiceImpl(PaymentsRepository paymentsRepository, ModelMapper modelMapper,
                               ApiOrdersClient apiOrdersClient, ApiNotificationsClient apiNotificationsClient, TransactionHistoryService transactionHistoryService, VNPayService vnPayService) {
        this.paymentsRepository = paymentsRepository;
        this.modelMapper = modelMapper;
        this.apiOrdersClient = apiOrdersClient;
        this.apiNotificationsClient = apiNotificationsClient;
        this.transactionHistoryService = transactionHistoryService;
        this.vnPayService = vnPayService;
    }

    @Override
        public List<PaymentsResponse> findAll() {
            // Lấy tất cả các payment từ PaymentsRepository
            List<Payments> paymentsList = paymentsRepository.findAll();

            // Lấy danh sách đơn hàng từ microservice Orders, kết quả là một trang (Page)
            ApiResponse<PageResponse<OrdersResponse>> ordersResponseApi = apiOrdersClient.findAll("", "", 1, 10);

            // Lấy danh sách đơn hàng từ page (content của PageResponse)
            List<OrdersResponse> ordersList = ordersResponseApi.getData().getContent();

            // Tạo một Map để ánh xạ từ orderId sang OrdersResponse
            Map<Integer, OrdersResponse> orderMap = new HashMap<>();
            for (OrdersResponse order : ordersList) {
                orderMap.put(order.getOrderId(), order);
            }

            // Tạo danh sách PaymentsResponse kết hợp dữ liệu thanh toán và thông tin đơn hàng
            List<PaymentsResponse> paymentsResponseList = new ArrayList<>();
            for (Payments payment : paymentsList) {
                PaymentsResponse paymentsResponse = modelMapper.map(payment, PaymentsResponse.class); // Ánh xạ từ Payments sang PaymentsResponse

                OrdersResponse order = orderMap.get(payment.getIdOrder());

                if (order != null) {
                    // Ánh xạ thêm thông tin đơn hàng vào PaymentsResponse
                    paymentsResponse.setStatusOrder(order.getStatus());
                    paymentsResponse.setFullNameOrder(order.getFullNameUsers());
                    paymentsResponse.setTotal(order.getTotal());
                }

                // Thêm paymentsResponse vào danh sách kết quả
                paymentsResponseList.add(paymentsResponse);
            }

            return paymentsResponseList;
        }

    @Override
    public PaymentsResponse findById(Integer id) {

        Payments payment = paymentsRepository.findById(id).orElseThrow(() -> new NotfoundException(MessageExceptionResponse.ID_PAYMENT_NOT_FOUND));  // Sử dụng findById của JpaRepository
        ApiResponse<OrdersResponse> orderResponseApi = apiOrdersClient.findById(payment.getIdOrder());
        if(orderResponseApi==null){
            throw new CustomException(MessageExceptionResponse.ORDER_NOT_FOUND);
        }

        OrdersResponse order = orderResponseApi.getData();

        PaymentsResponse paymentsResponse = modelMapper.map(payment, PaymentsResponse.class);

        paymentsResponse.setStatusOrder(order.getStatus());
        paymentsResponse.setFullNameOrder(order.getFullNameUsers());
        paymentsResponse.setTotal(order.getTotal());

        return paymentsResponse;
    }


    @Override
    public PaymentsRequest save(PaymentsRequest paymentsRequest) {
        ApiResponse<OrdersResponse> orderResponseApi = apiOrdersClient.findById(paymentsRequest.getIdOrder());
        if(orderResponseApi.getData()==null){
            throw new NotfoundException(MessageExceptionResponse.ORDER_NOT_FOUND);
        }
        Payments payments=modelMapper.map(paymentsRequest,Payments.class);
        payments.setDeletedAt(false);
        Payments paymentsSave=paymentsRepository.save(payments);
        return modelMapper.map(paymentsSave,PaymentsRequest.class);
    }

    @Override
    public PaymentsRequest update(Integer id, PaymentsRequest paymentsRequest) {
        Payments payments=paymentsRepository.findById(id).orElseThrow(() -> new NotfoundException(MessageExceptionResponse.ID_PAYMENT_NOT_FOUND));
        ApiResponse<OrdersResponse> orderResponseApi = apiOrdersClient.findById(paymentsRequest.getIdOrder());
        if(orderResponseApi.getData()==null){
            throw new CustomException(MessageExceptionResponse.ORDER_NOT_FOUND);
        }

        payments.setIdOrder(paymentsRequest.getIdOrder());
        payments.setPaymentDate(paymentsRequest.getPaymentDate());
        payments.setAmount(paymentsRequest.getAmount());
        payments.setPaymentMethod(paymentsRequest.getPaymentMethod());
        payments.setStatus(paymentsRequest.getStatus());
        Payments paymentsUpdate=paymentsRepository.save(payments);
        return modelMapper.map(paymentsUpdate,PaymentsRequest.class);
    }

    @Override
    public void delete(Integer id) {

    }

    @Override
    public String processPayment(PaymentsRequest paymentRequest, HttpServletRequest request) {
        // Bước 1: Kiểm tra đơn hàng
        ApiResponse<OrdersResponse> orderResponseApi = apiOrdersClient.findById(paymentRequest.getIdOrder());

        // Kiểm tra phản hồi từ API
        if (orderResponseApi == null || orderResponseApi.getData() == null) {
            throw new CustomException(MessageExceptionResponse.ORDER_NOT_FOUND);
        }

        OrdersResponse order = orderResponseApi.getData();

        // Kiểm tra trạng thái thanh toán của đơn hàng
        if (OrderStatus.SUCCESS.name().equals(order.getStatus())) {
            throw new CustomException(MessageExceptionResponse.ORDER_ALREADY_PAID);
        }

        // Bước 2: Kiểm tra số tiền thanh toán
        double epsilon = 1e-6; // Độ chính xác cho so sánh số thực

        // Kiểm tra số tiền thanh toán có đủ không
        if (paymentRequest.getAmount() < order.getTotal()) {
            throw new CustomException(MessageExceptionResponse.INSUFFICIENT_PAYMENT_AMOUNT);
        }

        // Kiểm tra số tiền thanh toán có hợp lệ không
        if (Math.abs(paymentRequest.getAmount() - order.getTotal()) > epsilon) {
            throw new CustomException(MessageExceptionResponse.INVALID_PAYMENT_AMOUNT);
        }

        // Bước 3: Tạo đơn thanh toán qua VNPay
        // Giả sử số tiền đã được chuyển đổi sang VND (có thể có phần thập phân)
        double amountInVND = paymentRequest.getAmount();

        long amountInVNDLong = Math.round(amountInVND * 100); // Ví dụ: 199.95 => 19995

        String orderInfo = String.valueOf(paymentRequest.getIdOrder());

        String baseUrl = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();

        String paymentUrl = vnPayService.createOrder(request, amountInVNDLong, orderInfo, baseUrl);

        return "redirect:" + paymentUrl;

    }

    @Override
    public String processPaymentSuccess(PaymentsRequest paymentRequest) {

        ApiResponse<OrdersResponse> orderResponseApi = apiOrdersClient.findById(paymentRequest.getIdOrder());

        OrdersResponse order = orderResponseApi.getData();
        // Tạo đối tượng Payments để lưu vào cơ sở dữ liệu
        Payments payments = new Payments();
        payments.setIdOrder(paymentRequest.getIdOrder());
        payments.setAmount(paymentRequest.getAmount());
        payments.setPaymentMethod(PaymentsMethod.VNPAY.name());  // Giả sử thanh toán qua thẻ
        payments.setPaymentDate(LocalDateTime.now());
        payments.setStatus(PaymentStatus.SUCCESS.name());

        // Lưu thông tin thanh toán vào cơ sở dữ liệu
        Payments savedPayment = paymentsRepository.save(payments);

        // Cập nhật trạng thái đơn hàng thành công
        apiOrdersClient.updateOrdersStatus(order.getOrderId(), OrderStatus.SUCCESS.name());

        // Lưu lịch sử giao dịch
        TransactionHistoryRequest transactionHistoryRequest = TransactionHistoryRequest.builder()
                .orderId(order.getOrderId())
                .userId(order.getUserId())
                .status("SUCCESS_History") // Trạng thái lịch sử giao dịch
                .build();
        transactionHistoryService.save(transactionHistoryRequest);

        // Gửi email thông báo cho khách hàng
        apiNotificationsClient.sendEmail(order.getEmailUser(),"Xin chào Khách Hàng",order.getOrderId());

        // Trả về kết quả thành công
        return "Payment processed successfully with ID: " + savedPayment.getPaymentId();
    }

}
