package io.devridge.admin.handler;

import io.devridge.admin.handler.ex.AdminCourseDetailException.AdminNotFoundCourseDetailException;
import lombok.extern.slf4j.Slf4j;

import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AdminCourseExceptionHandler {

    @ExceptionHandler(AdminNotFoundCourseDetailException.class)
    public String handleAdminCourseDetailNotFoundException(AdminNotFoundCourseDetailException e) {
        log.error("코스 상세를 찾을 수 없습니다. {}", e.getMessage(), e);

        return "redirect:/admin/item?error=not_found_course_detail";
    }
}
