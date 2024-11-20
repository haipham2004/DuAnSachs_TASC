package Du.An.Ban.Sach.Tasc.payment_service.service.impl;

import Du.An.Ban.Sach.Tasc.payment_service.dto.request.TransactionHistoryRequest;
import Du.An.Ban.Sach.Tasc.payment_service.entity.TransactionHistory;
import Du.An.Ban.Sach.Tasc.payment_service.repository.TransactionHistoryRepository;
import Du.An.Ban.Sach.Tasc.payment_service.service.TransactionHistoryService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class TransactionHistoryImpl implements TransactionHistoryService {

    private TransactionHistoryRepository transactionHistoryRepository;

    private ModelMapper modelMapper;

    @Autowired
    public TransactionHistoryImpl(TransactionHistoryRepository transactionHistoryRepository, ModelMapper modelMapper) {
        this.transactionHistoryRepository = transactionHistoryRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    public List<TransactionHistory> findAll() {
        return null;
    }

    @Override
    public TransactionHistoryRequest save(TransactionHistoryRequest transactionHistoryRequest) {
        TransactionHistory transactionHistory = modelMapper.map(transactionHistoryRequest, TransactionHistory.class);

        TransactionHistory transactionHistorySave = transactionHistoryRepository.save(transactionHistory);

        return modelMapper.map(transactionHistorySave, TransactionHistoryRequest.class);
    }

}
