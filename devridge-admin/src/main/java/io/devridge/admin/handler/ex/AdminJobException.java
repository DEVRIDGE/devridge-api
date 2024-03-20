package io.devridge.admin.handler.ex;

public class AdminJobException extends RuntimeException {

    private AdminJobException(String message) {
        super(message);
    }

    public static class AdminJobNotFoundException extends AdminJobException {
        public AdminJobNotFoundException() {
            super("해당 직무를 찾을 수 없습니다.");
        }
    }
}
