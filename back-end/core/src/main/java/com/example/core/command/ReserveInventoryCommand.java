package com.example.core.command;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.axonframework.modelling.command.TargetAggregateIdentifier;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReserveInventoryCommand {

    @TargetAggregateIdentifier
    private  String orderId;
    private  String bookId;
    private  int quantity;
}
