package com.weather.forecast.repository;

import com.weather.forecast.model.Forecast;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface ForecastRepository extends JpaRepository<Forecast, Long> {

    Optional<Forecast> findByForecastDate(LocalDate forecastDate);

    List<Forecast> findByForecastDateBetweenOrderByForecastDateAsc(LocalDate startDate, LocalDate endDate);

    List<Forecast> findByForecastDateGreaterThanEqualOrderByForecastDateAsc(LocalDate date);

    @Query("SELECT f FROM Forecast f LEFT JOIN FETCH f.zoneForecasts WHERE f.id = :id")
    Optional<Forecast> findByIdWithZoneForecasts(@Param("id") Long id);

    @Query("SELECT f FROM Forecast f LEFT JOIN FETCH f.zoneForecasts WHERE f.forecastDate = :date")
    Optional<Forecast> findByForecastDateWithZoneForecasts(@Param("date") LocalDate date);

    boolean existsByForecastDate(LocalDate forecastDate);
}