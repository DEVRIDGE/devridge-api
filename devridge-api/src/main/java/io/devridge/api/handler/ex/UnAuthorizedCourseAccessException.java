package io.devridge.api.handler.ex;

public class UnAuthorizedCourseAccessException extends RuntimeException{
    public UnAuthorizedCourseAccessException() {
        super("로그인 하지 않은 사용자에게 허용되지 않은 코스에 접근");
    }
}
