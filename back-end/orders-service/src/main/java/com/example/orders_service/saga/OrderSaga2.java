//package com.example.orders_service.saga;
//
//import com.example.orders_service.command.ProcessPaymentCommand;
//import com.example.core.command.UpdateOrderStatusCommand;
//import com.example.core.events.OrderCreatedEvent;
//import com.example.core.events.OrderStatusUpdatedEvent;
//import com.example.orders_service.event.PaymentFailedEvent;
//import com.example.orders_service.event.PaymentProcessedEvent;
//import org.axonframework.commandhandling.gateway.CommandGateway;
//import org.axonframework.modelling.saga.EndSaga;
//import org.axonframework.modelling.saga.SagaEventHandler;
//import org.axonframework.modelling.saga.SagaLifecycle;
//import org.axonframework.modelling.saga.StartSaga;
//import org.axonframework.spring.stereotype.Saga;
//
//@Saga
//public class OrderSaga {
//
//    private String orderId;
//    private String paymentMethod;
//    private String status;
//
//    private final CommandGateway commandGateway;
//
//    // Constructor
//    public OrderSaga(CommandGateway commandGateway) {
//        this.commandGateway = commandGateway;
//    }
//
//    @StartSaga
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handle(OrderCreatedEvent event) {
//        this.orderId = event.getOrderId();
//        this.paymentMethod = event.getPaymentMethod();
//        this.status = "CREATED";
//
//        // Gửi lệnh để xử lý thanh toán
//        ProcessPaymentCommand command = new ProcessPaymentCommand(orderId, paymentMethod, event.getTotalAmount());
//        commandGateway.send(command);
//    }
//
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handle(PaymentProcessedEvent event) {
//        // Thanh toán thành công, cập nhật trạng thái đơn hàng
//        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand(orderId, "PAID");
//        commandGateway.send(command);
//    }
//
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handle(PaymentFailedEvent event) {
//        // Thanh toán thất bại, cập nhật trạng thái đơn hàng
//        UpdateOrderStatusCommand command = new UpdateOrderStatusCommand(orderId, "PAYMENT_FAILED");
//        commandGateway.send(command);
//    }
//
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handle(OrderStatusUpdatedEvent event) {
//        // Đơn hàng đã có trạng thái cập nhật, saga hoàn tất
//        if ("PAID".equals(event.getStatus())) {
//            SagaLifecycle.end();  // Kết thúc Saga nếu thanh toán thành công
//        }
//    }
//
//    @EndSaga
//    @SagaEventHandler(associationProperty = "orderId")
//    public void handleOrderStatusUpdateEnd(OrderStatusUpdatedEvent event) {
//        // Kết thúc Saga khi trạng thái đơn hàng được cập nhật thành công
//        SagaLifecycle.end();
//    }
//}
