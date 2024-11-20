//package com.example.core.aggregate;
//
//import com.example.core.command.CreateOrderCommand;
//import com.example.core.command.UpdateOrderStatusCommand;
//import com.example.core.events.OrderCreatedEvent;
//import com.example.core.events.OrderItem;
//import com.example.core.events.OrderStatusUpdatedEvent;
//import org.axonframework.commandhandling.CommandHandler;
//import org.axonframework.eventhandling.EventHandler;
//import org.axonframework.modelling.command.AggregateLifecycle;
//import org.axonframework.modelling.command.AggregateIdentifier;
//import org.axonframework.spring.stereotype.Aggregate;
//
//import java.util.List;
//
//@Aggregate
//public class OrderAggregate {
//
//    @AggregateIdentifier
//    private String orderId;
//    private String userId;
//    private List<OrderItem> orderItem;
//    private double totalAmount;
//    private String shippingAddress;
//    private String status;
//
//    // Constructor mặc định (cần thiết cho Axon)
//    public OrderAggregate() {
//    }
//
//    // Lệnh tạo đơn hàng
//    @CommandHandler
//    public OrderAggregate(CreateOrderCommand command) {
//        this.orderId = command.getOrderId();
//        this.userId = command.getUserId();
//        this.orderItem = command.getOrderItem();
//        this.shippingAddress = command.getShippingAddress();
//        this.status = "PENDING";
//
//        this.totalAmount = calculateTotalAmount(command.getOrderItem());
//
//
//        AggregateLifecycle.apply(new OrderCreatedEvent(
//                command.getOrderId(),
//                command.getUserId(),
//                command.getOrderItem(),
//                this.totalAmount,
//                this.shippingAddress,
//                command.getPaymentMethod()
//        ));
//    }
//
//    @CommandHandler
//    public void handle(UpdateOrderStatusCommand command) {
//        this.status = command.getStatus();
//
//        AggregateLifecycle.apply(new OrderStatusUpdatedEvent(
//                command.getOrderId(),
//                command.getStatus()
//        ));
//    }
//
//    @EventHandler
//    public void on(OrderCreatedEvent event) {
//        this.orderId = event.getOrderId();
//        this.userId = event.getUserId();
//        this.orderItem = event.getOrderItem();
//        this.totalAmount = event.getTotalAmount();
//        this.shippingAddress = event.getShippingAddress();
//        this.status = "PENDING";
//    }
//
//    @EventHandler
//    public void on(OrderStatusUpdatedEvent event) {
//        this.status = event.getStatus();
//    }
//
//
//    private double calculateTotalAmount(List<OrderItem> orderItems) {
//        double total = 0.0;
//        for (OrderItem item : orderItems) {
//            total += item.getPrice() * item.getQuantity();
//        }
//        return total;
//    }
//
//    public String getOrderId() {
//        return orderId;
//    }
//
//    public void setOrderId(String orderId) {
//        this.orderId = orderId;
//    }
//
//    public String getUserId() {
//        return userId;
//    }
//
//    public void setUserId(String userId) {
//        this.userId = userId;
//    }
//
//    public List<OrderItem> getOrderItem() {
//        return orderItem;
//    }
//
//    public void setOrderItem(List<OrderItem> orderItem) {
//        this.orderItem = orderItem;
//    }
//
//    public double getTotalAmount() {
//        return totalAmount;
//    }
//
//    public void setTotalAmount(double totalAmount) {
//        this.totalAmount = totalAmount;
//    }
//
//    public String getShippingAddress() {
//        return shippingAddress;
//    }
//
//    public void setShippingAddress(String shippingAddress) {
//        this.shippingAddress = shippingAddress;
//    }
//
//    public String getStatus() {
//        return status;
//    }
//
//    public void setStatus(String status) {
//        this.status = status;
//    }
//}
//
//
//
////@Aggregate
////public class PaymentAggregate {
////
////    @AggregateIdentifier
////    private String orderId;
////
////    @CommandHandler
////    public PaymentAggregate(ProcessPaymentCommand command) {
////        // Logic xử lý thanh toán (giả định)
////        AggregateLifecycle.apply(new PaymentProcessedEvent(command.getOrderId(), command.getPaymentMethod(), command.getAmount()));
////    }
////
////    @CommandHandler
////    public void handle(ConfirmPaymentCommand command) {
////        // Xác nhận thanh toán
////        AggregateLifecycle.apply(new PaymentConfirmedEvent(command.getOrderId(), "SUCCESS"));
////    }
////}
//
//
//
////@Aggregate
////public class BookAggregate {
////
////    @AggregateIdentifier
////    private String bookId;
////    private int stock;
////
////    @CommandHandler
////    public BookAggregate(UpdateBookStockCommand command) {
////        // Giảm số lượng sách
////        this.stock += command.getQuantity();
////        AggregateLifecycle.apply(new BookStockUpdatedEvent(command.getBookId(), this.stock));
////    }
////}
