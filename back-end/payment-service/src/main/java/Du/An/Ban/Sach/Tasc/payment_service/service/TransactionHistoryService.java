package Du.An.Ban.Sach.Tasc.payment_service.service;

import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.entity.TransactionHistory;

import java.util.List;

public interface TransactionHistoryService {

    List<TransactionHistory> findAll();

    TransactionHistoryRequest save(TransactionHistoryRequest transactionHistoryRequest);

}
