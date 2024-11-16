package com.example.books_service.dto.request;

import jakarta.validation.constraints.Email;
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
public class PublishersRequest {

    private Integer publisherId;

    @NotBlank(message = "Name cannot be empty")
    @Size(min = 2, max = 100, message = "Name should be between 2 and 100 characters")
    private String name;

    @NotBlank(message = "Address cannot be empty")
    private String address;

    @NotBlank(message = "Phone number cannot be empty")
    @Pattern(regexp = "^0\\d{9}$", message = "Phone number must be 10 digits, start with 0, and contain only numbers")
    private String phone;

    @NotBlank(message = "email cannot be empty")
    @Email(message = "Email should be valid")
    private String email;
}
