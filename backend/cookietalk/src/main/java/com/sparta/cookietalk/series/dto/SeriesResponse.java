package com.sparta.cookietalk.series.dto;

import com.sparta.cookietalk.series.dto.SeriesResponse.Create;
import com.sparta.cookietalk.series.dto.SeriesResponse.List;
import com.sparta.cookietalk.series.entity.Series;

public sealed interface SeriesResponse permits Create, List {
    record Create(Long id, String title) implements SeriesResponse {}
    record List(Long id, String title) implements SeriesResponse {
        public List(Series series) {
            this(series.getId(), series.getTitle());
        }
    }
}
