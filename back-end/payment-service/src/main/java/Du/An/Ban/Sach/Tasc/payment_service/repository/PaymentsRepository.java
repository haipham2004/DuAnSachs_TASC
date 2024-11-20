package Du.An.Ban.Sach.Tasc.payment_service.repository;

import Du.An.Ban.Sach.Tasc.payment_service.entity.Payments;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface PaymentsRepository extends JpaRepository<Payments,Integer> {

}
