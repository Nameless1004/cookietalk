FROM openjdk:17-jdk-alpine
ARG JAR_FILE=./build/libs/cookietalk-0.0.1-SNAPSHOT.jar
COPY ${JAR_FILE} ./app.jar

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