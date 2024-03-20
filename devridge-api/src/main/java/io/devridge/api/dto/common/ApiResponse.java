package io.devridge.api.dto.common;

import lombok.Getter;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Getter
public class ApiResponse<T> {

    private static final String SUCCESS_STATUS = "success";
    private static final String ERROR_STATUS = "error";

    private final String status;
    private final String message;
    private final T data;

    private ApiResponse(String status, String message, T data) {
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public static <T> ApiResponse<T> success(T data) {
        return new ApiResponse<>(SUCCESS_STATUS, "요청 성공", data);
    }

    public static <T> ApiResponse<T> success(String message, T data) {
        return new ApiResponse<>(SUCCESS_STATUS, message, data);
    }

    public static ApiResponse<Object> success(String message) {
        return new ApiResponse<>(SUCCESS_STATUS, message, null);
    }

    public static ApiResponse<Object> error(String message) {
        return new ApiResponse<>(ERROR_STATUS, message, null);
    }

    public static ApiResponse<Map<String, String>> error(BindingResult bindingResult) {
        Map<String, String> errors = getErrors(bindingResult);
        return new ApiResponse<>(ERROR_STATUS, "유효성 검사 실패", errors);
    }

    private static Map<String, String> getErrors(BindingResult bindingResult) {
        Map<String, String> errors = new HashMap<>();

        List<ObjectError> allErrors = bindingResult.getAllErrors();
        for (ObjectError error : allErrors) {
            if (error instanceof FieldError) {
                errors.put(((FieldError) error).getField(), error.getDefaultMessage());
            } else {
                errors.put( error.getObjectName(), error.getDefaultMessage());
            }
        }
        return errors;
    }
}
