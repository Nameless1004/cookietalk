package com.sparta.cookietalk.common.exceptions;

public class S3UploadFailedException extends RuntimeException{

    public S3UploadFailedException(){
        super("S3 업로드에 실패하셨습니다.");
    }
}
