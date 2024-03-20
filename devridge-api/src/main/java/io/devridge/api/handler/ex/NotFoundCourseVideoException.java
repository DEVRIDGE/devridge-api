package io.devridge.api.handler.ex;

public class NotFoundCourseVideoException extends RuntimeException{
    public NotFoundCourseVideoException() {
        super("존재하지 않는 코스 영상입니다.");
    }
}
