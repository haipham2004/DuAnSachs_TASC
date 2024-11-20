//package com.example.orders_service.saga;
//
//import com.example.core.event.OrderCreatedEvent;
//import lombok.extern.slf4j.Slf4j;
//import org.axonframework.commandhandling.CommandCallback;
//import org.axonframework.commandhandling.CommandMessage;
//import org.axonframework.commandhandling.CommandResultMessage;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.axonframework.modelling.saga.SagaEventHandler;
//import org.axonframework.modelling.saga.StartSaga;
//import org.axonframework.spring.stereotype.Saga;
//
//@Saga
//@Slf4j
//public class OrderSaga {
//
//    private transient CommandGateway commandGateway;
//
//    @StartSaga
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handle(OrderCreatedEvent orderCreatedEvent) {
//
//        ReverseBookCommand reverseBookCommand = ReverseBookCommand.builder()
//                .orderId(orderCreatedEvent.getOrderId())
//                .bookId(orderCreatedEvent.getBookId())
//                .quantity(orderCreatedEvent.getQuantity())
//                .userId(orderCreatedEvent.getUserId())
//                .build();
//
//        log.info("OrderCreateEvent handle for orderr id: " + reverseBookCommand.getOrderId() + "and product od" + reverseBookCommand.getBookId());
//        commandGateway.send(reverseBookCommand, new CommandCallback<ReverseBookCommand, Object>() {
//            @Override
//            public void onResult(CommandMessage<? extends ReverseBookCommand> commandMessage, CommandResultMessage<?> commandResultMessage) {
//                if (commandResultMessage.isExceptional()) {
//
//                }
//            }
//        });
//    }
//
//
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handle(BookReservedEvent bookReservedEvent) {
//        log.info("OrderCreateEvent handle for orderr id: " + bookReservedEvent.getOrderId() + "and product od" + bookReservedEvent.getBookId());
//    }
//
//    @SagaEventHandler(associationProperty = "paymentId")
//    public void handle(PaymentProcessedEvent paymentProcessedEvent) {
//
//    }
//}
