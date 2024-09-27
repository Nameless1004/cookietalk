package com.sparta.cookietalk.amazon;

import com.sparta.cookietalk.common.enums.UploadType;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.concurrent.CompletableFuture;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Slf4j(topic = "HLS")
@Component
public class HlsConverter {

    @Value("${hls.ffmpeg.path}")
    private String ffmpegPath;
    @Value("${hls.ffprobe.path}")
    private String ffprobePath;

    @Value("${hls.output.path}")
    private String hlsOutputPath;

    private FFprobe ffprobe;
    private FFmpeg ffmpeg;

    @PostConstruct
    public void init() {
        try{
            ffmpeg = new FFmpeg(ffmpegPath);
            ffprobe = new FFprobe(ffprobePath);
            log.info("ffmpeg 생성완료!");
            log.info("ffprobe 생성완료!");
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage(), e);
        }
    }

    /**
     *
     * @param uploadType
     * @param localFile
     * @return 아웃풋 폴더 패스 반환
     */
    @Async
    public CompletableFuture<File> convert(UploadType uploadType, File localFile) {
        try {
            String name = localFile.getName();
            name = getFilename(name); // 확장자 제거 이름 추출

            File outputPath = new File(hlsOutputPath + File.separator + name);
            if (!outputPath.exists()) {
                outputPath.mkdirs();
            }

            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(localFile.getAbsolutePath())
                .addOutput(outputPath.getAbsolutePath() + File.separator + "video.m3u8")
                .addExtraArgs("-profile:v", "baseline")
                .addExtraArgs("-level", "3.0")
                .addExtraArgs("-start_number", "0")
                .addExtraArgs("-hls_time", "10")
                .addExtraArgs("-hls_list_size", "0")
                .addExtraArgs("-f", "hls")
                .done();

            FFmpegExecutor executor = new FFmpegExecutor(ffmpeg, ffprobe);
            executor.createJob(builder)
                .run();

//        // 동영상 썸네일 생성
//        FFmpegBuilder builderThumbNail = new FFmpegBuilder()
//            .overrideOutputFiles(true) // 오버라이드 여부
//            .setInput(localFile.getAbsolutePath())
//            .addExtraArgs("-ss", "00:00:03") // 썸네일 추출 시작점
//            .addOutput(outputPath.getAbsolutePath() + File.separator + "thumbnail.png") // 썸네일 경로
//            .setFrames(1) // 프레임 수
//            .done();
//        FFmpegExecutor executorThumbNail = new FFmpegExecutor(ffmpeg, ffprobe);
//        executorThumbNail.createJob(builderThumbNail).run();
            return CompletableFuture.completedFuture(outputPath);
        } finally {
            localFile.delete();
        }
    }

    private String getFilename(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf(".");
        return originalFilename.substring(0, lastDotIndex);
    }
}
