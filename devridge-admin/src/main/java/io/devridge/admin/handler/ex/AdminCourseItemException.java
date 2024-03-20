package io.devridge.admin.handler.ex;

import lombok.Getter;

public class AdminCourseItemException extends RuntimeException {

    private AdminCourseItemException(String message) {
        super(message);
    }

    @Getter
    public static class AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException extends AdminCourseItemException {
        private final Long courseDetailId;

        public AdminNotMatchedCourseDetailAndCourseVideoOfCourseDetailException(Long courseDetailId, Long courseVideoId, Long courseVideoDetailId) {
            super(courseDetailId + "에 해당 코스 디테일과 " + courseVideoId + "에 해당 코스 비디오가 일치하지 않습니다. 코스 비디오 디테일 ID: " + courseVideoDetailId);
            this.courseDetailId = courseDetailId;
        }
    }

    @Getter
    public static class AdminNotFoundCourseVideoException extends AdminCourseItemException {
        private final Long courseDetailId;
        private final String exceptionStatusCode;

        public AdminNotFoundCourseVideoException(Long courseVideoId, Long courseDetailId, String exceptionStatusCode) {
            super(courseVideoId + "에 해당 코스 비디오를 찾을 수 없습니다.");
            this.courseDetailId = courseDetailId;
            this.exceptionStatusCode = exceptionStatusCode;
        }
    }

    @Getter
    public static class AdminNotFoundCourseBookException extends AdminCourseItemException {
        private final Long courseDetailId;
        private final String exceptionStatusCode;

        public AdminNotFoundCourseBookException(Long courseBookId, Long courseDetailId, String exceptionStatusCode) {
            super(courseBookId + "에 해당 코스 책를 찾을 수 없습니다.");
            this.courseDetailId = courseDetailId;
            this.exceptionStatusCode = exceptionStatusCode;
        }
    }
}
