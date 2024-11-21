package com.example.orders_service.aggregate;

import com.example.orders_service.command.CreateOrderCommand;
import com.example.orders_service.dto.request.OrdersItemsRequest;
import com.example.orders_service.dto.response.OrderStatus;
import com.example.orders_service.event.CreatedOrderEvent;
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

    private List<OrdersItemsRequest> items;

    private OrderStatus orderStatus;

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
                .items(command.getItems())
                .orderStatus(OrderStatus.CREATED)
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
        this.items = event.getItems();
        this.orderStatus = event.getOrderStatus();
    }
}
