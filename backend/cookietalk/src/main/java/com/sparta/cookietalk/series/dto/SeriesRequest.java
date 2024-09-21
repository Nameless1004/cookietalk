package com.sparta.cookietalk.series.dto;

import com.sparta.cookietalk.series.dto.SeriesRequest.Create;
import com.sparta.cookietalk.series.dto.SeriesRequest.Patch;

public sealed interface SeriesRequest permits Create, Patch {
    record Create(String title) implements  SeriesRequest {}

    record Patch(String title) implements SeriesRequest { }
}
