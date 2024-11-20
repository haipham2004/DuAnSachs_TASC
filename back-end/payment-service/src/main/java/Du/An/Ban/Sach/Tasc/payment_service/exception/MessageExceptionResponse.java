package Du.An.Ban.Sach.Tasc.payment_service.exception;

public class MessageExceptionResponse {

    public static final String ID_PAYMENT_NOT_FOUND = "ID payment không tồn tại";
    public static final String NAME_NOT_FOUND = "Tên không tồn tại";
    public static final String PAYMENT_NOT_FOUND = "Thanh toán không tồn tại";
    public static final String ORDER_NOT_FOUND = "Đơn hàng không tồn tại";
    public static final String INVALID_PAYMENT_METHOD = "Phương thức thanh toán không hợp lệ";
    public static final String INTERNAL_SERVER_ERROR = "Lỗi máy chủ nội bộ, vui lòng thử lại sau";


    public static final String ORDER_ALREADY_PAID = "Đơn hàng đã được thanh toán.";
    public static final String INSUFFICIENT_PAYMENT_AMOUNT = "Số tiền thanh toán không đủ.";
    public static final String INVALID_PAYMENT_AMOUNT = "Số tiền thanh toán không hợp lệ.";

}
