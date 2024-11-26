//package com.example.books_service.handle;
//
//import com.example.books_service.dto.request.BooksRequest;
//import com.example.books_service.service.BooksService;
//import com.example.common_service.command.CancelBookReservationCommand;
//import com.example.common_service.command.ReserveBookCommand;
//import com.example.common_service.event.BookReservedEvent;
//import com.example.common_service.event.BooksReservationFailedEvent;
//import com.example.common_service.event.OrdersItemsCreatedEvent;
//import com.example.common_service.exception.BookInsufficientQuantityException;
//import lombok.extern.slf4j.Slf4j;
//import org.slf4j.Logger;
//import org.slf4j.LoggerFactory;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.kafka.annotation.KafkaHandler;
//import org.springframework.kafka.annotation.KafkaListener;
//import org.springframework.kafka.core.KafkaTemplate;
//import org.springframework.messaging.handler.annotation.Payload;
//import org.springframework.stereotype.Component;
//
//@Component
//@Slf4j
//@KafkaListener(topics = "products-commands")
//public class BookCommandsHandler {
//
//    private final BooksService booksService;
//    private final Logger logger = LoggerFactory.getLogger(this.getClass());
//    private final KafkaTemplate<String, Object> kafkaTemplate;
//    private final String productEventsTopicName;
//
//    public BookCommandsHandler(BooksService booksService,
//                               KafkaTemplate<String, Object> kafkaTemplate,
//                               @Value("${products.events.topic.name}") String productEventsTopicName) {
//        this.booksService = booksService;
//        this.kafkaTemplate = kafkaTemplate;
//        this.productEventsTopicName = productEventsTopicName;
//    }
//
//    @KafkaHandler
//    public void handleBookReservedEvent(@Payload ReserveBookCommand command) {
//        log.info("Received command nhé bạn: {}", command);
//        System.out.println("Ahhi");
//        log.info("Handle Revers Bookss...");
//
//        try {
//            BooksRequest desiredProduct = BooksRequest.builder()
//                    .bookId(command.getBookId())
//                    .quantity(command.getQuantity())
//                    .build();
//
//            BooksRequest reservedProduct = booksService.reserve(desiredProduct, command.getOrderId());
//
//            BookReservedEvent productReservedEvent = new BookReservedEvent(
//                    command.getOrderId(),
//                    command.getBookId(),
//                    command.getQuantity(),
//                    reservedProduct.getPrice()
//            );
//
//            kafkaTemplate.send(productEventsTopicName, productReservedEvent);
//
//        } catch (Exception e) {
//            logger.info("Insufficient quantity for book ID {} in order ID {}. Reason: {}",
//                    command.getBookId(), command.getOrderId(), e.getMessage(), e);
//
//            BooksReservationFailedEvent booksReservationFailedEvent = new BooksReservationFailedEvent(
//                    command.getBookId(), command.getOrderId(), command.getQuantity());
//
//            kafkaTemplate.send(productEventsTopicName, booksReservationFailedEvent);
//
//        }
//    }
//
//
//    @KafkaHandler
//    public void handleCommand(@Payload CancelBookReservationCommand command) {
//        // Tạo đối tượng BooksRequest từ CancelBookReservationCommand
//        BooksRequest productToCancel = BooksRequest.builder()
//                .bookId(command.getBookId())     // Lấy bookId từ command
//                .quantity(command.getQuantity()) // Lấy quantity từ command
//                .build();
//
//        // Gọi service để hủy đặt sách/sản phẩm
//        booksService.cancelReservation(productToCancel, command.getOrderId());
//
//        // Tạo sự kiện hủy đặt sách/sản phẩm
//        BookReservedEvent productReservationCancelledEvent =
//                new BookReservedEvent(command.getBookId(), command.getOrderId());
//
//        // Gửi sự kiện vào topic Kafka
//        kafkaTemplate.send(productEventsTopicName, productReservationCancelledEvent);
//
//        // Log thông báo (tùy chọn)
//        log.info("Sent BookReservedEvent for Order ID: {} and Book ID: {}",
//                command.getOrderId(), command.getBookId());
//    }
//
//
//}
