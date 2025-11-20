package com.weather.forecast.repository;

import com.weather.forecast.model.ZoneForecast;
import com.weather.forecast.model.ZoneForecast.ForecastPeriod;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneForecastRepository extends JpaRepository<ZoneForecast, Long> {

    List<ZoneForecast> findByZoneId(Long zoneId);

    List<ZoneForecast> findByForecastId(Long forecastId);

    List<ZoneForecast> findByZoneIdAndPeriod(Long zoneId, ForecastPeriod period);

    @Query("SELECT zf FROM ZoneForecast zf WHERE zf.zone.id = :zoneId " +
            "AND zf.forecast.id = :forecastId")
    Optional<ZoneForecast> findByZoneIdAndForecastId(
            @Param("zoneId") Long zoneId,
            @Param("forecastId") Long forecastId
    );

    @Query("SELECT zf FROM ZoneForecast zf WHERE zf.zone.id = :zoneId " +
            "AND zf.period = :period AND zf.forecast.id = :forecastId")
    Optional<ZoneForecast> findByZoneAndPeriodAndForecast(
            @Param("zoneId") Long zoneId,
            @Param("period") ForecastPeriod period,
            @Param("forecastId") Long forecastId
    );
}