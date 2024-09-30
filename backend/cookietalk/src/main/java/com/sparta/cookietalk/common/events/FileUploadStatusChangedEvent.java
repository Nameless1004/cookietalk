package com.sparta.cookietalk.common.events;

import lombok.Getter;
import org.springframework.context.ApplicationEvent;

@Getter
public class FileUploadStatusChangedEvent extends ApplicationEvent {

    private final Long uploadFileId;

    public FileUploadStatusChangedEvent(Object source, Long uploadFileId) {
        super(source);
        this.uploadFileId = uploadFileId;
    }
}
