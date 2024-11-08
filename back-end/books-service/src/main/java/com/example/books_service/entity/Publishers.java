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

public class Publishers {

    private Integer publisherId;

    private String name;

    private String address;

    private String phone;

    private String email;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private Boolean deletedAt;
}
