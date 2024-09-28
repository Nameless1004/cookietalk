package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.common.dto.ResponseDto;
import com.sparta.cookietalk.common.enums.UploadType;
import com.sparta.cookietalk.upload.dto.UploadFileResponse;
import com.sparta.cookietalk.upload.dto.UploadFileResponse.Detail;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.multipart.MultipartFile;

@Controller
@RequiredArgsConstructor
public class UploadController {

    private final UploadService uploadService;

    @GetMapping("/api/uploads")
    public String test(){
        return "upload";
    }

//    @PostMapping("/api/uploads/videos")
//    @ResponseBody
//    public ResponseEntity<ResponseDto<UploadFileResponse.Detail>> uploadVideo( @RequestPart("video") MultipartFile video){
//        Detail detail = uploadService.uploadVideo(null, video);
//        return ResponseDto.toEntity(HttpStatus.OK, detail);
//    }
//
//    @DeleteMapping("/api/uploads/files/{fileId}")
//    @ResponseBody
//    public ResponseEntity<ResponseDto<Void>> uploadVideo(@PathVariable("fileId") Long fileId) {
//        uploadService.deleteFile(null, fileId);
//        return ResponseEntity.ok(ResponseDto.of(HttpStatus.OK, "비디오 삭제에 성공하였습니다."));
//    }

//    @PostMapping("/api/uploads/image")
//    public String uploadImg(@AuthenticationPrincipal UserDetailsImpl userDetails, @RequestParam("image") MultipartFile file) {
//        uploadService.upload(userDetails.getUser(), UploadType.IMAGE, file);
//    }
}
