package com.sparta.cookietalk.series.repository;

import com.sparta.cookietalk.series.entity.Series;
import com.sparta.cookietalk.user.entity.User;
import java.util.List;
import java.util.Optional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface SeriesRepository extends JpaRepository<Series, Long> {

    @Query("select s from Series s join fetch s.channel where s.id = :id")
    Optional<Series> findWithChannelById(@Param("id") Long id);

    @Query("SELECT s FROM Series s JOIN FETCH s.channel JOIN FETCH s.channel.user WHERE s.id = :id")
    Optional<Series> findWithChannelAndUserById(@Param("id") Long seriesId);

    @Query("SELECT s FROM Series s JOIN FETCH s.channel JOIN FETCH s.channel.user WHERE s.channel.user=:user")
    List<Series> findAllByUser(@Param("user") User user);
}
