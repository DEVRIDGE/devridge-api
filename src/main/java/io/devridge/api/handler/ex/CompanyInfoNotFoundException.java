package io.devridge.api.handler.ex;

public class CompanyInfoNotFoundException extends RuntimeException{
    public CompanyInfoNotFoundException(String message) {
        super(message);
    }
}
