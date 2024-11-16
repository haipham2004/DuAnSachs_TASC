package com.example.books_service.dto.response;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
@ToString
public class AuthorsResponse {

    private Integer id;

    private String name;

    private String phone;

    private String addRess;

    private Boolean deletedAt;
}
