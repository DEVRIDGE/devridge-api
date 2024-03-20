package io.devridge.api.handler.ex;

public class NotFoundCourseBookException extends RuntimeException{
    public NotFoundCourseBookException() {
        super("존재하지 않는 코스 책입니다.");
    }
}
