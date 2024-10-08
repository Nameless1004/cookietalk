package com.sparta.cookietalk.common.exceptions;

public class FileUploadInProgressException extends UploadException {
    public FileUploadInProgressException() {
        super("파일 업로드 중입니다. 잠시만 기다려주세요.");
    }
}
