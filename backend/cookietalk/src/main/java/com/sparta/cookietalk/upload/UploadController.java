package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.common.enums.UploadType;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @GetMapping("/api/uploads")
    public String test(){
        return "upload";
    }

    @PostMapping("/api/uploads/videos")
    public ResponseEntity<Void> uploadVideo( @RequestPart("video") MultipartFile video){
        uploadService.uploadVideo(null, video);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/api/uploads/files/{fileId}")
    public ResponseEntity<Void> uploadVideo(@PathVariable("fileId") Long fileId) {
        uploadService.deleteFile(null, fileId);
        return ResponseEntity.ok().build();
    }

//    @PostMapping("/api/uploads/image")
//    public String uploadImg(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("image") MultipartFile file) {
//        uploadService.upload(userDetails.getUser(), UploadType.IMAGE, file);
//    }
}
