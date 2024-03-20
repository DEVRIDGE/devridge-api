package io.devridge.api.handler.ex;

public class CourseDetailNotFoundException extends RuntimeException{
    public CourseDetailNotFoundException(String message) {
        super(message);
    }
}
