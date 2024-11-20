package Du.An.Ban.Sach.Tasc.payment_service.service;

import Du.An.Ban.Sach.Tasc.payment_service.dto.request.PaymentsRequest;
import Du.An.Ban.Sach.Tasc.payment_service.dto.response.PaymentsResponse;

import java.util.List;

public interface PaymentsService {

    List<PaymentsResponse> findAll();

    PaymentsResponse findById(Integer id);

    PaymentsRequest save(PaymentsRequest paymentsRequest);

    PaymentsRequest update(Integer id, PaymentsRequest paymentsRequest);

    void delete(Integer id);

     String processPayment(PaymentsRequest paymentRequest);
}
