package com.sparta.cookietalk.upload;

import com.sparta.cookietalk.common.events.FileUploadStatusChangedEvent;
import com.sparta.cookietalk.cookie.service.CookieService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class FileUploadEventListener {

    private final CookieService cookieService;

    @EventListener
    public void handleFileUploadStatusChanged(FileUploadStatusChangedEvent event) {
        cookieService.onFileUploadStatusChanged(event.getUploadFileId());
    }
}
