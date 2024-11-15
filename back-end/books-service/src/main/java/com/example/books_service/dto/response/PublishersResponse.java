package com.example.books_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class PublishersResponse {

    private Integer id;

    private String name;

    private String address;

    private String phone;

    private String email;

    private Boolean deletedAt;
}
