package com.example.core.event;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class ReserveInventoryEvent {

    private  String orderId;
    private  String bookId;
    private  int quantity;
}
