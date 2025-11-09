package com.weather.forecast.repository;

import com.weather.forecast.model.WeatherForecast;
import com.weather.forecast.model.WeatherForecast.ForecastPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface WeatherForecastRepository extends JpaRepository<WeatherForecast, Long> {

    List<WeatherForecast> findByZoneId(Long zoneId);

    List<WeatherForecast> findByZoneIdAndPeriod(Long zoneId, ForecastPeriod period);

    @Query("SELECT wf FROM WeatherForecast wf WHERE wf.zone.id = :zoneId " +
            "AND wf.forecastDate >= :startDate AND wf.forecastDate <= :endDate")
    List<WeatherForecast> findByZoneAndDateRange(
            @Param("zoneId") Long zoneId,
            @Param("startDate") LocalDate startDate,
            @Param("endDate") LocalDate endDate
    );

    @Query("SELECT wf FROM WeatherForecast wf WHERE wf.zone.id = :zoneId " +
            "AND wf.forecastDate = :date AND wf.period = :period")
    Optional<WeatherForecast> findByZoneAndDateAndPeriod(
            @Param("zoneId") Long zoneId,
            @Param("date") LocalDate date,
            @Param("period") ForecastPeriod period
    );

    List<WeatherForecast> findByForecastDateGreaterThanEqualOrderByForecastDateAsc(LocalDate date);
}