package com.weather.forecast.service;

import com.weather.forecast.dto.WeatherForecastDTO;
import com.weather.forecast.model.Administrator;
import com.weather.forecast.model.WeatherForecast;
import com.weather.forecast.model.WeatherForecast.ForecastPeriod;
import com.weather.forecast.model.Zone;
import com.weather.forecast.repository.WeatherForecastRepository;
import com.weather.forecast.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class WeatherForecastService {

    @Autowired
    private WeatherForecastRepository forecastRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public WeatherForecastDTO createForecast(WeatherForecastDTO forecastDTO) {
        Zone zone = zoneRepository.findById(forecastDTO.getZoneId())
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        Administrator admin = authService.getCurrentAdmin();

        // Validar que la temperatura máxima sea mayor que la mínima
        if (forecastDTO.getMaxTemperature() < forecastDTO.getMinTemperature()) {
            throw new RuntimeException("La temperatura máxima debe ser mayor que la mínima");
        }

        // Validar que la velocidad máxima del viento sea mayor que la mínima
        if (forecastDTO.getMaxWindSpeed() < forecastDTO.getMinWindSpeed()) {
            throw new RuntimeException("La velocidad máxima del viento debe ser mayor que la mínima");
        }

        WeatherForecast forecast = new WeatherForecast();
        forecast.setZone(zone);
        forecast.setForecastDate(forecastDTO.getForecastDate());
        forecast.setPeriod(forecastDTO.getPeriod());
        forecast.setMinTemperature(forecastDTO.getMinTemperature());
        forecast.setMaxTemperature(forecastDTO.getMaxTemperature());
        forecast.setMinWindSpeed(forecastDTO.getMinWindSpeed());
        forecast.setMaxWindSpeed(forecastDTO.getMaxWindSpeed());
        forecast.setPrecipitation(forecastDTO.getPrecipitation());
        forecast.setCloudiness(forecastDTO.getCloudiness());
        forecast.setObservations(forecastDTO.getObservations());

        WeatherForecast savedForecast = forecastRepository.save(forecast);
        return convertToDTO(savedForecast);
    }

    @Transactional
    public WeatherForecastDTO updateForecast(Long id, WeatherForecastDTO forecastDTO) {
        WeatherForecast forecast = forecastRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pronóstico no encontrado"));

        // Validaciones
        if (forecastDTO.getMaxTemperature() < forecastDTO.getMinTemperature()) {
            throw new RuntimeException("La temperatura máxima debe ser mayor que la mínima");
        }

        if (forecastDTO.getMaxWindSpeed() < forecastDTO.getMinWindSpeed()) {
            throw new RuntimeException("La velocidad máxima del viento debe ser mayor que la mínima");
        }

        forecast.setForecastDate(forecastDTO.getForecastDate());
        forecast.setPeriod(forecastDTO.getPeriod());
        forecast.setMinTemperature(forecastDTO.getMinTemperature());
        forecast.setMaxTemperature(forecastDTO.getMaxTemperature());
        forecast.setMinWindSpeed(forecastDTO.getMinWindSpeed());
        forecast.setMaxWindSpeed(forecastDTO.getMaxWindSpeed());
        forecast.setPrecipitation(forecastDTO.getPrecipitation());
        forecast.setCloudiness(forecastDTO.getCloudiness());
        forecast.setObservations(forecastDTO.getObservations());

        WeatherForecast updatedForecast = forecastRepository.save(forecast);
        return convertToDTO(updatedForecast);
    }

    @Transactional(readOnly = true)
    public WeatherForecastDTO getForecastById(Long id) {
        WeatherForecast forecast = forecastRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Pronóstico no encontrado"));
        return convertToDTO(forecast);
    }

    @Transactional(readOnly = true)
    public List<WeatherForecastDTO> getAllForecasts() {
        return forecastRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeatherForecastDTO> getForecastsByZone(Long zoneId) {
        return forecastRepository.findByZoneId(zoneId).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeatherForecastDTO> getForecastsByZoneAndPeriod(Long zoneId, ForecastPeriod period) {
        return forecastRepository.findByZoneIdAndPeriod(zoneId, period).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeatherForecastDTO> getForecastsByZoneAndDateRange(Long zoneId, LocalDate startDate, LocalDate endDate) {
        return forecastRepository.findByZoneAndDateRange(zoneId, startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<WeatherForecastDTO> getCurrentAndFutureForecasts() {
        LocalDate today = LocalDate.now();
        return forecastRepository.findByForecastDateGreaterThanEqualOrderByForecastDateAsc(today).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteForecast(Long id) {
        if (!forecastRepository.existsById(id)) {
            throw new RuntimeException("Pronóstico no encontrado");
        }
        forecastRepository.deleteById(id);
    }

    private WeatherForecastDTO convertToDTO(WeatherForecast forecast) {
        WeatherForecastDTO dto = new WeatherForecastDTO();
        dto.setId(forecast.getId());
        dto.setZoneId(forecast.getZone().getId());
        dto.setZoneName(forecast.getZone().getName());
        dto.setForecastDate(forecast.getForecastDate());
        dto.setPeriod(forecast.getPeriod());
        dto.setMinTemperature(forecast.getMinTemperature());
        dto.setMaxTemperature(forecast.getMaxTemperature());
        dto.setMinWindSpeed(forecast.getMinWindSpeed());
        dto.setMaxWindSpeed(forecast.getMaxWindSpeed());
        dto.setPrecipitation(forecast.getPrecipitation());
        dto.setCloudiness(forecast.getCloudiness());
        dto.setObservations(forecast.getObservations());
        dto.setCreatedAt(forecast.getCreatedAt());
        dto.setUpdatedAt(forecast.getUpdatedAt());
        return dto;
    }
}