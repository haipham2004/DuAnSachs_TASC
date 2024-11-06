package com.example.users_service.exception;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class MyHandler extends ResponseEntityExceptionHandler {

    @ExceptionHandler
    public static ResponseEntity<ErrolResponse> globalExceptionHandler(Exception ex) {
        ErrolResponse ers = new ErrolResponse(HttpStatus.BAD_REQUEST.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(ers);
    }

    @ExceptionHandler
    public ResponseEntity<ErrolResponse> ResourceNotfoundUsers(ResourceNotfound ex) {
        ErrolResponse ers = new ErrolResponse(HttpStatus.NOT_FOUND.value(), ex.getMessage());
        return ResponseEntity.status(HttpStatus.NOT_FOUND).body(ers);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(MethodArgumentNotValidException ex,
                                                                  HttpHeaders headers,
                                                                  HttpStatusCode status,
                                                                  WebRequest request) {
        // Tạo response chứa thông tin lỗi
        Map<String, Object> response = new HashMap<>();

        // Tạo một map để lưu các lỗi cụ thể cho từng trường
        Map<String, String> errors = new HashMap<>();

        // Duyệt qua các lỗi trong BindingResult và lấy tên trường + thông báo lỗi
        ex.getBindingResult().getAllErrors().forEach(error -> {
            // Lấy tên trường có lỗi
            String fieldName = ((FieldError) error).getField();

            // Lấy thông điệp lỗi từ validation
            String message = error.getDefaultMessage();

            // Lưu tên trường và thông điệp lỗi vào Map
            errors.put(fieldName, message);
        });

        // Trả về mã lỗi HTTP 400 (Bad Request) và thông tin chi tiết lỗi
        response.put("status", HttpStatus.BAD_REQUEST.value());
        response.put("error", "Bad request");
        response.put("messageValidation", errors);  // Lưu các lỗi vào "message"

        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(response);
    }


}
