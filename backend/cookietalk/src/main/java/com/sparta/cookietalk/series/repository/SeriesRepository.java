package com.sparta.cookietalk.series.repository;

import com.sparta.cookietalk.series.entity.Series;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("select s from Series s join fetch s.channel where s.id = :id")
    Optional<Series> findWithChannelById(@Param("id") Long id);
}
