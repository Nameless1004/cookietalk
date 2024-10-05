package com.sparta.cookietalk.common.dto;

import com.sparta.cookietalk.common.dto.Response.Page;
import com.sparta.cookietalk.common.dto.Response.Slice;
import java.time.LocalDateTime;
import java.util.List;

public sealed interface Response permits Page, Slice {
    record Page<T>(
        List<T> contents,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
    ) implements Response {
        public Page(org.springframework.data.domain.Page<T> page) {
           this(page.getContent(), page.getNumber(), page.getSize(), page.getTotalElements(), page.getTotalPages());
        }
    }

    record Slice<T>(
        List<T> contents,
        boolean hasNextPage,
        int size,
        LocalDateTime nextCursor
    ) implements Response{ }

}
