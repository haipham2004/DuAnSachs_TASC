package Du.An.Ban.Sach.Tasc.payment_service.mapper;

import org.modelmapper.ModelMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ModalMapper {

    @Bean
    public ModelMapper modelMapper(){
        return new ModelMapper();
    }
}
