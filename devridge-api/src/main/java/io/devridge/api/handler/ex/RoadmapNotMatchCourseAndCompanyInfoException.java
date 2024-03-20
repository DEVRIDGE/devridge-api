package io.devridge.api.handler.ex;

public class RoadmapNotMatchCourseAndCompanyInfoException extends RuntimeException {
    public RoadmapNotMatchCourseAndCompanyInfoException() {
        super("로드맵에 코스와 회사정보에 일치하는 정보가 없습니다.");
    }
}
