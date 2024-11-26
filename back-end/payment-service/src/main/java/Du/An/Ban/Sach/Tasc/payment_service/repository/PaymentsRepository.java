package Du.An.Ban.Sach.Tasc.payment_service.repository;

import Du.An.Ban.Sach.Tasc.payment_service.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments,Integer> {

    @Transactional
    @Modifying
    @Query(value = "UPDATE Payments p SET p.status = :status WHERE p.idOrder = :idOrder")
    void updatePaymentStatus(@Param("idOrder") Integer idOrder, @Param("status") String status);
}
