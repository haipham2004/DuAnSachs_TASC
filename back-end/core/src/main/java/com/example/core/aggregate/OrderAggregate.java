package com.example.core.aggregate;


import com.example.core.command.CreateOrderCommand;
import com.example.core.dto.response.OrderStatus2;
import com.example.core.event.CreatedOrderEvent;
import com.example.core.dto.request.OrderItem2;
import org.axonframework.commandhandling.CommandHandler;
import org.axonframework.eventsourcing.EventSourcingHandler;
import org.axonframework.modelling.command.AggregateIdentifier;
import org.axonframework.modelling.command.AggregateLifecycle;
import org.axonframework.spring.stereotype.Aggregate;

import java.util.List;

@Aggregate
public class OrderAggregate {

    @AggregateIdentifier
    private Integer orderId;

    private Integer userId;

    private Double totalAmount;

    private String trackingNumber;

    private String shippingAddress;

    private String paymentMethod;

    private List<OrderItem2> items;

    private OrderStatus2 orderStatus2;

    public OrderAggregate() {
    }

    @CommandHandler
    public OrderAggregate(CreateOrderCommand command) {

        CreatedOrderEvent createdOrderEvent = CreatedOrderEvent.builder()
                .orderId(command.getOrderId())
                .userId(command.getUserId())
                .totalAmount(command.getTotalAmount())
                .trackingNumber(null)
                .shippingAddress(command.getShippingAddress())
                .paymentMethod(command.getPaymentMethod())
                .items(command.getItems())
                .orderStatus2(OrderStatus2.CREATED)
                .build();

        AggregateLifecycle.apply(createdOrderEvent);
    }

    @EventSourcingHandler
    public void on(CreatedOrderEvent event) {
        this.orderId = event.getOrderId();
        this.userId = event.getUserId();
        this.totalAmount = event.getTotalAmount();
        this.trackingNumber = event.getTrackingNumber();
        this.shippingAddress = event.getShippingAddress();
        this.paymentMethod = event.getPaymentMethod();
        this.items = event.getItems();
        this.orderStatus2 = event.getOrderStatus2();
    }
}
