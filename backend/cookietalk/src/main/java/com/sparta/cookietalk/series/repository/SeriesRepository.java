package com.sparta.cookietalk.series.repository;

import com.sparta.cookietalk.series.entity.Series;
import org.springframework.data.jpa.repository.JpaRepository;

public interface SeriesRepository extends JpaRepository<Series, Long> {

}
