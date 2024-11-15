package com.example.books_service.entity;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class Authors {

    private Integer authorId;

    private String name;

    private String phone;

    private String addRess;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deletedAt;

}
