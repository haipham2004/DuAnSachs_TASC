package com.example.books_service.dto.request;

import jakarta.validation.constraints.DecimalMin;
import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.List;

@AllArgsConstructor
@NoArgsConstructor
@Getter
@Setter
public class BooksRequest {

    private Integer bookId;

    @NotBlank(message = "Title cannot be blank")  // Validate trường này không được trống
    @Size(max = 255, message = "Title cannot exceed 255 characters")  // Độ dài tối đa của title là 255 ký tự
    private String title;

    @NotNull(message = "Author ID cannot be null")
    private Integer authorId;

    @NotNull(message = "Publisher ID cannot be null")
    private Integer publisherId;

    @NotNull(message = "Category ID cannot be null")
    private Integer categoryId;

    @NotNull(message = "Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Price must be a positive number")
    private Double price;

    @NotNull(message = "Cons Price cannot be null")
    @DecimalMin(value = "0.0", inclusive = true, message = "Cons Price must be a positive number")
    private Double consPrice;

    @NotBlank(message = "Description cannot be blank")
    @Size(max = 1000, message = "Description cannot exceed 1000 characters")
    private String description;

    @Min(value = 0, message = "Quantity must be at least 0")
    private Integer quantity;

    private String status;

    @NotNull(message = "Image URLs cannot be null")
    @Size(min = 1, message = "At least one image URL must be provided")
    private List<String> imageUrl;

    @NotBlank(message = "Thumbnail cannot be blank")  
    private String thumbnail;
}
