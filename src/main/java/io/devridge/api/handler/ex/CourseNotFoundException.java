package io.devridge.api.handler.ex;

public class CourseNotFoundException extends RuntimeException{
    public CourseNotFoundException(String message) {
        super(message);
    }
}
