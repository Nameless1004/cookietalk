# 1. Build stage: Gradle을 사용하여 빌드
FROM gradle:7.5.1-jdk17 AS build
WORKDIR /app

# 소스 코드 전체를 컨테이너로 복사
COPY . .

# Gradle 빌드 수행 (bootJar 태스크를 실행하여 jar 파일 생성)
RUN gradle bootJar

# 2. Run stage: 빌드된 JAR 파일을 실행할 경량화된 이미지 사용
FROM openjdk:17-jdk-alpine
WORKDIR /app

# 빌드 단계에서 생성된 jar 파일을 런타임 컨테이너로 복사
COPY --from=build /app/build/libs/*.jar app.jar

# ffmpeg 설치
RUN apk update && apk add --no-cache ffmpeg
ENV SPRING_DATASOURCE_URL=jdbc:mysql://mysql:3306/cookietalk
ENV SPRING_DATASOURCE_USERNAME=root
ENV SPRING_DATASOURCE_PASSWORD=example
ENV REDIS_LOCALHOST_NAME=redis
ENV FFMPEG_PATH=/usr/bin/ffmpeg
ENV FFPROBE_PATH=/usr/bin/ffprobe
ENV HLS_OUTPUT=/temp/videos/hls-output
ENV LOCAL_SAVE=/temp/save
RUN mkdir -p "/temp/videos/hls-output"
RUN mkdir -p "/temp/save"
ENTRYPOINT ["java", "-jar", "-Dspring.profiles.active=prod", "./app.jar"]