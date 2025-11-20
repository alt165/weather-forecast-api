package com.weather.forecast.service;

import com.weather.forecast.dto.ForecastDTO;
import com.weather.forecast.dto.ZoneForecastDTO;
import com.weather.forecast.model.Administrator;
import com.weather.forecast.model.Forecast;
import com.weather.forecast.model.Zone;
import com.weather.forecast.model.ZoneForecast;
import com.weather.forecast.repository.ForecastRepository;
import com.weather.forecast.repository.ZoneForecastRepository;
import com.weather.forecast.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.time.LocalDate;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ForecastService {

    @Autowired
    private ForecastRepository forecastRepository;

    @Autowired
    private ZoneForecastRepository zoneForecastRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private AuthService authService;

    @Transactional
    public ForecastDTO createForecast(ForecastDTO forecastDTO) {
        // Verificar que no exista un pronóstico para esa fecha
        if (forecastRepository.existsByForecastDate(forecastDTO.getForecastDate())) {
            throw new RuntimeException("Ya existe un pronóstico para la fecha: " + forecastDTO.getForecastDate());
        }

        Administrator admin = authService.getCurrentAdmin();

        // Crear el pronóstico general
        Forecast forecast = new Forecast();
        forecast.setForecastDate(forecastDTO.getForecastDate());
        forecast.setGeneralObservations(forecastDTO.getGeneralObservations());
        forecast.setAlerts(forecastDTO.getAlerts());
        forecast.setCreatedBy(admin);

        // Crear los pronósticos por zona
        for (ZoneForecastDTO zfDTO : forecastDTO.getZoneForecasts()) {
            Zone zone = zoneRepository.findById(zfDTO.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zona no encontrada: " + zfDTO.getZoneId()));

            // Validaciones
            if (zfDTO.getMaxTemperature() < zfDTO.getMinTemperature()) {
                throw new RuntimeException("La temperatura máxima debe ser mayor que la mínima en zona: " + zone.getName());
            }

            if (zfDTO.getMaxWindSpeed() < zfDTO.getMinWindSpeed()) {
                throw new RuntimeException("La velocidad máxima del viento debe ser mayor que la mínima en zona: " + zone.getName());
            }

            ZoneForecast zoneForecast = new ZoneForecast();
            zoneForecast.setZone(zone);
            zoneForecast.setPeriod(zfDTO.getPeriod());
            zoneForecast.setMinTemperature(zfDTO.getMinTemperature());
            zoneForecast.setMaxTemperature(zfDTO.getMaxTemperature());
            zoneForecast.setMinWindSpeed(zfDTO.getMinWindSpeed());
            zoneForecast.setMaxWindSpeed(zfDTO.getMaxWindSpeed());
            zoneForecast.setPrecipitation(zfDTO.getPrecipitation());
            zoneForecast.setCloudiness(zfDTO.getCloudiness());
            zoneForecast.setZoneObservations(zfDTO.getZoneObservations());

            forecast.addZoneForecast(zoneForecast);
        }

        Forecast savedForecast = forecastRepository.save(forecast);
        return convertToDTO(savedForecast);
    }

    @Transactional
    public ForecastDTO updateForecast(Long id, ForecastDTO forecastDTO) {
        Forecast forecast = forecastRepository.findByIdWithZoneForecasts(id)
                .orElseThrow(() -> new RuntimeException("Pronóstico no encontrado"));

        // Actualizar datos generales
        forecast.setForecastDate(forecastDTO.getForecastDate());
        forecast.setGeneralObservations(forecastDTO.getGeneralObservations());
        forecast.setAlerts(forecastDTO.getAlerts());

        // Limpiar pronósticos de zona existentes
        forecast.getZoneForecasts().clear();

        // Agregar nuevos pronósticos de zona
        for (ZoneForecastDTO zfDTO : forecastDTO.getZoneForecasts()) {
            Zone zone = zoneRepository.findById(zfDTO.getZoneId())
                    .orElseThrow(() -> new RuntimeException("Zona no encontrada: " + zfDTO.getZoneId()));

            // Validaciones
            if (zfDTO.getMaxTemperature() < zfDTO.getMinTemperature()) {
                throw new RuntimeException("La temperatura máxima debe ser mayor que la mínima en zona: " + zone.getName());
            }

            if (zfDTO.getMaxWindSpeed() < zfDTO.getMinWindSpeed()) {
                throw new RuntimeException("La velocidad máxima del viento debe ser mayor que la mínima en zona: " + zone.getName());
            }

            ZoneForecast zoneForecast = new ZoneForecast();
            zoneForecast.setZone(zone);
            zoneForecast.setPeriod(zfDTO.getPeriod());
            zoneForecast.setMinTemperature(zfDTO.getMinTemperature());
            zoneForecast.setMaxTemperature(zfDTO.getMaxTemperature());
            zoneForecast.setMinWindSpeed(zfDTO.getMinWindSpeed());
            zoneForecast.setMaxWindSpeed(zfDTO.getMaxWindSpeed());
            zoneForecast.setPrecipitation(zfDTO.getPrecipitation());
            zoneForecast.setCloudiness(zfDTO.getCloudiness());
            zoneForecast.setZoneObservations(zfDTO.getZoneObservations());

            forecast.addZoneForecast(zoneForecast);
        }

        Forecast updatedForecast = forecastRepository.save(forecast);
        return convertToDTO(updatedForecast);
    }

    @Transactional(readOnly = true)
    public ForecastDTO getForecastById(Long id) {
        Forecast forecast = forecastRepository.findByIdWithZoneForecasts(id)
                .orElseThrow(() -> new RuntimeException("Pronóstico no encontrado"));
        return convertToDTO(forecast);
    }

    @Transactional(readOnly = true)
    public ForecastDTO getForecastByDate(LocalDate date) {
        Forecast forecast = forecastRepository.findByForecastDateWithZoneForecasts(date)
                .orElseThrow(() -> new RuntimeException("No hay pronóstico para la fecha: " + date));
        return convertToDTO(forecast);
    }

    @Transactional(readOnly = true)
    public List<ForecastDTO> getAllForecasts() {
        return forecastRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ForecastDTO> getForecastsByDateRange(LocalDate startDate, LocalDate endDate) {
        return forecastRepository.findByForecastDateBetweenOrderByForecastDateAsc(startDate, endDate).stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ForecastDTO> getCurrentAndFutureForecasts() {
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

    private ForecastDTO convertToDTO(Forecast forecast) {
        ForecastDTO dto = new ForecastDTO();
        dto.setId(forecast.getId());
        dto.setForecastDate(forecast.getForecastDate());
        dto.setGeneralObservations(forecast.getGeneralObservations());
        dto.setAlerts(forecast.getAlerts());
        dto.setCreatedAt(forecast.getCreatedAt());
        dto.setUpdatedAt(forecast.getUpdatedAt());
        dto.setCreatedBy(forecast.getCreatedBy().getUsername());

        List<ZoneForecastDTO> zoneForecastDTOs = forecast.getZoneForecasts().stream()
                .map(this::convertZoneForecastToDTO)
                .collect(Collectors.toList());
        dto.setZoneForecasts(zoneForecastDTOs);

        return dto;
    }

    private ZoneForecastDTO convertZoneForecastToDTO(ZoneForecast zoneForecast) {
        ZoneForecastDTO dto = new ZoneForecastDTO();
        dto.setId(zoneForecast.getId());
        dto.setZoneId(zoneForecast.getZone().getId());
        dto.setZoneName(zoneForecast.getZone().getName());
        dto.setPeriod(zoneForecast.getPeriod());
        dto.setMinTemperature(zoneForecast.getMinTemperature());
        dto.setMaxTemperature(zoneForecast.getMaxTemperature());
        dto.setMinWindSpeed(zoneForecast.getMinWindSpeed());
        dto.setMaxWindSpeed(zoneForecast.getMaxWindSpeed());
        dto.setPrecipitation(zoneForecast.getPrecipitation());
        dto.setCloudiness(zoneForecast.getCloudiness());
        dto.setZoneObservations(zoneForecast.getZoneObservations());
        return dto;
    }
}