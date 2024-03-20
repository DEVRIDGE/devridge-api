package io.devridge.admin.handler.ex;

public class AdminCourseDetailException extends RuntimeException {

    private AdminCourseDetailException(String message) {
        super(message);
    }

    public static class AdminNotFoundCourseDetailException extends AdminCourseDetailException {
        public AdminNotFoundCourseDetailException(Long courseDetailId) {
            super(courseDetailId + "에 해당 코스를 찾을 수 없습니다.");
        }
    }
}
