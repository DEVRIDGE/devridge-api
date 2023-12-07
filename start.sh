#!/bin/sh

# 프로젝트 루트 디렉토리에서 Gradle 빌드 실행
./gradlew build

# JAR 파일의 경로 (변경 가능)
JAR_PATH="./build/libs/devridge-1.2.2-SNAPSHOT.jar"

# Java 실행 명령
java -jar $JAR_PATH