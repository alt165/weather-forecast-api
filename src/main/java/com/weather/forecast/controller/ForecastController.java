package com.weather.forecast.controller;

import com.weather.forecast.dto.ForecastDTO;
import com.weather.forecast.service.ForecastService;
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
public class ForecastController {

    @Autowired
    private ForecastService forecastService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ForecastDTO> createForecast(@Valid @RequestBody ForecastDTO forecastDTO) {
        try {
            ForecastDTO createdForecast = forecastService.createForecast(forecastDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdForecast);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ForecastDTO> updateForecast(
            @PathVariable Long id,
            @Valid @RequestBody ForecastDTO forecastDTO) {
        try {
            ForecastDTO updatedForecast = forecastService.updateForecast(id, forecastDTO);
            return ResponseEntity.ok(updatedForecast);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    public ResponseEntity<ForecastDTO> getForecastById(@PathVariable Long id) {
        try {
            ForecastDTO forecast = forecastService.getForecastById(id);
            return ResponseEntity.ok(forecast);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/date/{date}")
    public ResponseEntity<ForecastDTO> getForecastByDate(
            @PathVariable @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        try {
            ForecastDTO forecast = forecastService.getForecastByDate(date);
            return ResponseEntity.ok(forecast);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ForecastDTO>> getAllForecasts() {
        List<ForecastDTO> forecasts = forecastService.getAllForecasts();
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/range")
    public ResponseEntity<List<ForecastDTO>> getForecastsByDateRange(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate startDate,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate endDate) {
        List<ForecastDTO> forecasts = forecastService.getForecastsByDateRange(startDate, endDate);
        return ResponseEntity.ok(forecasts);
    }

    @GetMapping("/current")
    public ResponseEntity<List<ForecastDTO>> getCurrentAndFutureForecasts() {
        List<ForecastDTO> forecasts = forecastService.getCurrentAndFutureForecasts();
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