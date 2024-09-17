package com.sparta.cookietalk.common.utils;

import com.sparta.cookietalk.common.enums.UploadType;
import java.io.File;
import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.Lock;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
public class FileUtils {

    public File saveTemp(UploadType uploadType, MultipartFile file) {
        try {
            String ext = getExtension(file.getOriginalFilename());
            String fileName = getFilename(file.getOriginalFilename());
            String uuid = UUID.randomUUID()
                .toString();
            Path path = Paths.get(uploadType.getLocalSavePath(), uuid + "." + ext);
            File tempFile = path.toFile();
            if (!tempFile.exists()) {
                tempFile.mkdirs();
            }
            file.transferTo(tempFile);
            return tempFile;
        } catch (IOException o) {
           throw new RuntimeException(o.getMessage());
        }
    }

    public String getFilename(String originalFilename) {
        int lastDotIndex = originalFilename.lastIndexOf(".");
        if(lastDotIndex < 0) return originalFilename;
        return originalFilename.substring(0, lastDotIndex);
    }

    public String getExtension(String originalFilename) {
        int i = originalFilename.lastIndexOf(".");
        return originalFilename.substring(i + 1);
    }

    /**
     * 파일을 재귀적으로 삭제합니다. 하위 파일을 다 삭제 후 지정한 파일을 삭제합니다.
     * @param file
     */
    public void deleteFileRecur(File file){
        if(file == null || !file.exists()){
            return;
        }

        File[] files = file.listFiles();

        if(files == null){
            file.delete();
            return;
        }

        for(File f : files){
            deleteFileRecur(f);
        }

        file.delete();
    }
}
