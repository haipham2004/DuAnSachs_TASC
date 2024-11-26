package com.example.notifications_service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class MessageDto {

    private Integer idOrder;

    private String to;

    private String toName;

    private String toSubject;

    private String content;
}
