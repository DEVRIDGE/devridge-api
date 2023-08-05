package io.devridge.api.handler.ex;

public class CompanyJobNotFoundException extends RuntimeException{
    public CompanyJobNotFoundException(String message) {
        super(message);
    }
}
