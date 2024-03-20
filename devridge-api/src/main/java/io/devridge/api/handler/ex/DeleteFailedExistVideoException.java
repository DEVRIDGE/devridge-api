package io.devridge.api.handler.ex;

public class DeleteFailedExistVideoException extends RuntimeException{
    public DeleteFailedExistVideoException() {
        super("해당 코스에 영상이 존재해서 삭제할 수 없습니다.");
    }
}
