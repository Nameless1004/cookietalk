package com.sparta.cookietalk.upload.components;

import com.sparta.cookietalk.common.utils.FileUtils;
import io.jsonwebtoken.security.Jwks.OP;
import jakarta.annotation.PostConstruct;
import java.io.File;
import java.io.IOException;
import java.util.Optional;
import lombok.extern.slf4j.Slf4j;
import net.bramp.ffmpeg.FFmpeg;
import net.bramp.ffmpeg.FFmpegExecutor;
import net.bramp.ffmpeg.FFprobe;
import net.bramp.ffmpeg.builder.FFmpegBuilder;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Slf4j(topic = "HLS")
@Component
public class HlsConverter implements Converter<File, Optional<File>> {

    private final FileUtils fileUtils;
    @Value("${hls.ffmpeg.path}")
    private String ffmpegPath;
    @Value("${hls.ffprobe.path}")
    private String ffprobePath;

    @Value("${hls.output.path}")
    private String hlsOutputPath;

    private FFprobe ffprobe;
    private FFmpeg ffmpeg;

    public HlsConverter(FileUtils fileUtils) {
        this.fileUtils = fileUtils;
    }


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

    private String getFilename(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf(".");
        return originalFilename.substring(0, lastDotIndex);
    }

    @Override
    public Optional<File> convert(File file) {
        String name = file.getName();
        name = getFilename(name); // 확장자 제거 이름 추출

        File outputPath = new File(hlsOutputPath + File.separator + name);
        if (!outputPath.exists()) {
            outputPath.mkdirs();
        }

        try {
            FFmpegBuilder builder = new FFmpegBuilder()
                .setInput(file.getAbsolutePath())
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

            return Optional.of(outputPath);
        } catch (Exception e) {
            // 문제 생기면 파일 삭제
            fileUtils.deleteFileRecur(outputPath);
            return Optional.empty();
        }
        finally {
            // 변환 후 임시파일 삭제
            file.delete();
        }
    }
}
