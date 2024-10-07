package com.sparta.cookietalk.common.dto;

import com.sparta.cookietalk.common.dto.Response.Page;
import com.sparta.cookietalk.common.dto.Response.Slice;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.data.domain.Pageable;

public sealed interface Response permits Page, Slice {
    record Page<T>(
        List<T> contents,
        int pageNumber,
        int pageSize,
        long totalElements,
        int totalPages
    ) implements Response {
        public static <T>Page<T> of(List<T> contents, Long count, Pageable pageable) {
            count = count == null ? 0 : count;
            double ceil = Math.ceil(count.floatValue() / (float) pageable.getPageSize());
           return new Page<>(contents, pageable.getPageNumber() + 1, pageable.getPageSize(), count, (int) ceil);
        }
    }

    record Slice<T>(
        List<T> contents,
        boolean hasNextPage,
        int size,
        LocalDateTime nextCursor
    ) implements Response{ }

}
