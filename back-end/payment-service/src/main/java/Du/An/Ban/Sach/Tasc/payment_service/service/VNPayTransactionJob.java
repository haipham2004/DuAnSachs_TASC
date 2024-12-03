package Du.An.Ban.Sach.Tasc.payment_service.service;

import Du.An.Ban.Sach.Tasc.payment_service.client.ApiOrdersClient;
import Du.An.Ban.Sach.Tasc.payment_service.entity.Payments;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


@Slf4j
@Service
public class VNPayTransactionJob {

    private final PaymentsService paymentsService;
    private final ExecutorService executorService;

    @Autowired
    public VNPayTransactionJob(PaymentsService paymentsService) {
        this.paymentsService = paymentsService;
        this.executorService = Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors() * 2); // Tối đa 2x số CPU
    }

    // Cron job chạy mỗi 5 phút (300,000 ms)
    @Scheduled(fixedRate = 300000)
    public void checkPendingTransactions() {
        log.info("Cron job bắt đầu kiểm tra các giao dịch PENDING...");

        // Lấy tất cả giao dịch có trạng thái "PENDING"
        List<Payments> pendingTransactions = paymentsService.findByStatus("PENDING");

        log.info("Tổng số giao dịch PENDING tìm thấy: {}", pendingTransactions.size());

        for (Payments transaction : pendingTransactions) {
            executorService.submit(() -> handleTransaction(transaction));
        }
    }

    private void handleTransaction(Payments transaction) {
        try {
            long currentTime = System.currentTimeMillis();
            LocalDateTime transactionTime = transaction.getPaymentDate(); // Lấy thời gian thanh toán của giao dịch

            // Chuyển LocalDateTime sang Instant để tính toán
            ZonedDateTime zonedDateTime = transactionTime.atZone(ZoneId.systemDefault());
            long transactionTimeMillis = zonedDateTime.toInstant().toEpochMilli();

            log.info("Kiểm tra giao dịch với Order ID: {}. Thời gian thanh toán: {} (Millis: {})",
                    transaction.getIdOrder(), transactionTime, transactionTimeMillis);

            // Kiểm tra nếu giao dịch đã vượt quá thời gian timeout của VNPay (15 phút)
            long timeElapsed = currentTime - transactionTimeMillis;
            long remainingTime = (15 * 60 * 1000) - timeElapsed;  // Thời gian còn lại tính bằng milliseconds

            // Tính toán thời gian còn lại theo phút
            long remainingMinutes = remainingTime / (60 * 1000);
            long remainingSeconds = (remainingTime % (60 * 1000)) / 1000;  // Thời gian còn lại theo giây

            // Log thời gian còn lại
            if (remainingMinutes > 0 || remainingSeconds > 0) {
                log.info("Giao dịch Order ID: {} còn lại {} phút và {} giây trước khi hết thời gian chờ.",
                        transaction.getIdOrder(), remainingMinutes, remainingSeconds);
            }

            if (timeElapsed > (15 * 60 * 1000)) {
                log.warn("Giao dịch Order ID: {} đã vượt quá thời gian chờ 15 phút và sẽ được đánh dấu là 'FAILED'.", transaction.getIdOrder());
                paymentsService.updatePaymentStatus(transaction.getIdOrder(), "FAILED");
            } else {
                log.info("Giao dịch Order ID: {} vẫn trong thời gian hợp lệ.", transaction.getIdOrder());
            }

        } catch (Exception e) {
            log.error("Lỗi khi xử lý giao dịch Order ID: {}", transaction.getIdOrder(), e);
        }
    }
}


