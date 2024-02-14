# 사용할 JDK 11 베이스 이미지를 지정합니다.
FROM adoptopenjdk/openjdk11:alpine-jre

# 컨테이너 내부에서 애플리케이션 파일을 저장할 디렉토리를 생성합니다.
WORKDIR /app

# 빌드한 실행 가능한 JAR 파일을 현재 디렉토리에 복사합니다.
COPY build/libs/devridge-*.jar app.jar

# 컨테이너가 시작될 때 실행될 명령어를 지정합니다.
ENTRYPOINT ["java", "-jar", "-Dspring.config.location=file:/config/", "app.jar"]
