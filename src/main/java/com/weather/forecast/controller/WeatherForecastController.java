package com.weather.forecast.controller;

import com.weather.forecast.dto.WeatherForecastDTO;
import com.weather.forecast.model.WeatherForecast.ForecastPeriod;
import com.weather.forecast.service.WeatherForecastService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/forecasts")
@CrossOrigin(origins = "*", maxAge = 3600)
public class WeatherForecastController {

    @Autowired
    private WeatherForecastService forecastService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WeatherForecastDTO> createForecast(@Valid @RequestBody WeatherForecastDTO forecastDTO) {
        try {
            WeatherForecastDTO createdForecast = forecastService.createForecast(forecastDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdForecast);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WeatherForecastDTO> updateForecast(
            @PathVariable Long id,
            @Valid @RequestBody WeatherForecastDTO forecastDTO) {
        try {
            WeatherForecastDTO updatedForecast = forecastService.updateForecast(id, forecastDTO);
            return ResponseEntity.ok(updatedForecast);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<WeatherForecastDTO> getForecastById(@PathVariable Long id) {
        try {
            WeatherForecastDTO forecast = forecastService.getForecastById(id);
            return ResponseEntity.ok(forecast);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<WeatherForecastDTO>> getAllForecasts() {
        List<WeatherForecastDTO> forecasts = forecastService.getAllForecasts();
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/zone/{zoneId}")
    public ResponseEntity<List<WeatherForecastDTO>> getForecastsByZone(@PathVariable Long zoneId) {
        List<WeatherForecastDTO> forecasts = forecastService.getForecastsByZone(zoneId);
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/zone/{zoneId}/period/{period}")
    public ResponseEntity<List<WeatherForecastDTO>> getForecastsByZoneAndPeriod(
            @PathVariable Long zoneId,
            @PathVariable ForecastPeriod period) {
        List<WeatherForecastDTO> forecasts = forecastService.getForecastsByZoneAndPeriod(zoneId, period);
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/zone/{zoneId}/range")
    public ResponseEntity<List<WeatherForecastDTO>> getForecastsByZoneAndDateRange(
            @PathVariable Long zoneId,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<WeatherForecastDTO> forecasts = forecastService.getForecastsByZoneAndDateRange(zoneId, startDate, endDate);
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/public/current")
    public ResponseEntity<List<WeatherForecastDTO>> getCurrentAndFutureForecasts() {
        List<WeatherForecastDTO> forecasts = forecastService.getCurrentAndFutureForecasts();
        return ResponseEntity.ok(forecasts);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteForecast(@PathVariable Long id) {
        try {
            forecastService.deleteForecast(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}