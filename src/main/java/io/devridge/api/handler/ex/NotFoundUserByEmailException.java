package io.devridge.api.handler.ex;

public class NotFoundUserByEmailException extends RuntimeException {
    public NotFoundUserByEmailException(String email) {
        super("이메일에 해당하는 유저를 찾을 수 없습니다. email : " + email);
    }
}
