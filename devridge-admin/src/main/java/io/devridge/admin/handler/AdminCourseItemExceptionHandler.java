package io.devridge.admin.handler;

import io.devridge.admin.handler.common.ExceptionStatusCode;
import io.devridge.admin.handler.ex.AdminCourseItemException.AdminNotFoundCourseBookException;
import io.devridge.admin.handler.ex.AdminCourseItemException.AdminNotFoundCourseVideoException;
import io.devridge.admin.handler.ex.AdminCourseItemException.AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class AdminCourseItemExceptionHandler {

    @ExceptionHandler(AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException.class)
    public String handleAdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException(AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException e) {
        log.error("코스 디테일과 코스 비디오가 일치하지 않습니다. {}", e.getMessage(), e);

        return "redirect:/admin/item/" + e.getCourseDetailId() + "?error=fail_modified";
    }

    @ExceptionHandler(AdminNotFoundCourseVideoException.class)
    public String handleAdminNotFoundCourseVideoException(AdminNotFoundCourseVideoException e) {
        log.error("코스 비디오를 찾을 수 없습니다. {}", e.getMessage(), e);

        String errorParam = "fail_modified";
        if (e.getExceptionStatusCode().equals(ExceptionStatusCode.COURSE_VIDEO_DELETE)) {
            errorParam = "fail_deleted";
        }

        return "redirect:/admin/item/" + e.getCourseDetailId() + "?error=" + errorParam;
    }

    @ExceptionHandler(AdminNotFoundCourseBookException.class)
    public String handleAdminNotFoundCourseBookException(AdminNotFoundCourseBookException e) {
        log.error("코스 책을 찾을 수 없습니다. {}", e.getMessage(), e);

        String errorParam = "fail_modified";
        if (e.getExceptionStatusCode().equals(ExceptionStatusCode.COURSE_BOOK_DELETE)) {
            errorParam = "fail_deleted";
        }

        return "redirect:/admin/item/" + e.getCourseDetailId() + "?error=" + errorParam + "&isActivate=true";
    }
}
