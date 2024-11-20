package Du.An.Ban.Sach.Tasc.payment_service.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient("NOTIFICATIONS-SERVICE")
public interface ApiNotificationsClient {

    @GetMapping("/notifications/sendEmailOrserSuccess")
     String sendEmail(
            @RequestParam("to") String to,
            @RequestParam("subject") String subject,
            @RequestParam("idOrder") Integer idOrder

    );
}
