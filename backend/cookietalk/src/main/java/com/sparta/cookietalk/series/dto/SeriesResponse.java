package com.sparta.cookietalk.series.dto;

import com.sparta.cookietalk.series.dto.SeriesResponse.Create;

public sealed interface SeriesResponse permits Create {
    record Create(Long id, String title) implements SeriesResponse {}
}
