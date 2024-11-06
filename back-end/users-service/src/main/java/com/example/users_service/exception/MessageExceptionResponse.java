package com.example.users_service.exception;

public class MessageExceptionResponse {

    public static final String USERNAME_ALREADY_IN_USE = "Username already exists, please enter a different username.";
    public static final String EMAIL_ALREADY_IN_USE = "Email already exists, please enter a different email.";
    public static final String PHONE_ALREADY_IN_USE = "Phone number already exists, please enter a different phone number.";
    public static final String INVALID_CREDENTIALS = "Invalid username or password. Please try again.";
    public static final String PASSWORD_TOO_WEAK = "Password is too weak. Please use a stronger password.";
    public static final String INVALID_REFRESH_TOKEN =" Refresh token không hợp lệ. Vui lòng đăng nhập lại để lấy token mới.";
    public static final String USER_NOT_FOUND="Người dùng không tìm thấy. Vui lòng kiểm tra lại thông tin đăng nhập.";
    public static final String UNAUTHORIZED = "Full authentication is required to access this resource.";
    public static final String TOKEN_NOT_FOUND = "Refresh token not found for the user."; // Thêm dòng này
}
