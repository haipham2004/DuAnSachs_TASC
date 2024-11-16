package com.example.books_service.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class AuthorsRequest {

    private Integer authorId;

    @NotBlank(message = "Name cannot be null")
    @Size(min = 2, max = 50, message = "Name must be between 2 and 50 characters")
    private String name;

    @NotBlank(message = "phone cannot be null")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must be 10 digits, start with 0, and contain only numbers")
    private String phone;

    @NotBlank(message = "addRess cannot be null")
    private String addRess;
}
