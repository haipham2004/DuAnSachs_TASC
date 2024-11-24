package Du.An.Ban.Sach.Tasc.payment_service.repository;

import Du.An.Ban.Sach.Tasc.payment_service.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments,Integer> {

    @Query(value = "UPDATE payments SET status = :status WHERE orderId = :orderId AND paymentId = :paymentId", nativeQuery = true)
    void updatePaymentStatus(@Param("orderId") Integer idOrder, @Param("paymentId") Integer paymentId, @Param("status") String status);

}
