package com.weather.forecast.dto;

import com.weather.forecast.model.WeatherForecast.ForecastPeriod;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class WeatherForecastDTO {

    private Long id;

    @NotNull(message = "El ID de la zona es obligatorio")
    private Long zoneId;

    private String zoneName;

    @NotNull(message = "La fecha del pronóstico es obligatoria")
    private LocalDate forecastDate;

    @NotNull(message = "El período del pronóstico es obligatorio")
    private ForecastPeriod period;

    @NotNull(message = "La temperatura mínima es obligatoria")
    @DecimalMin(value = "-50.0", message = "La temperatura mínima debe ser mayor a -50°C")
    @DecimalMax(value = "60.0", message = "La temperatura mínima debe ser menor a 60°C")
    private Double minTemperature;

    @NotNull(message = "La temperatura máxima es obligatoria")
    @DecimalMin(value = "-50.0", message = "La temperatura máxima debe ser mayor a -50°C")
    @DecimalMax(value = "60.0", message = "La temperatura máxima debe ser menor a 60°C")
    private Double maxTemperature;

    @NotNull(message = "La velocidad mínima del viento es obligatoria")
    @DecimalMin(value = "0.0", message = "La velocidad mínima del viento debe ser mayor o igual a 0")
    private Double minWindSpeed;

    @NotNull(message = "La velocidad máxima del viento es obligatoria")
    @DecimalMin(value = "0.0", message = "La velocidad máxima del viento debe ser mayor o igual a 0")
    private Double maxWindSpeed;

    @NotNull(message = "La precipitación es obligatoria")
    @DecimalMin(value = "0.0", message = "La precipitación debe ser mayor o igual a 0")
    private Double precipitation;

    @NotNull(message = "La nubosidad es obligatoria")
    @Min(value = 0, message = "La nubosidad debe ser mínimo 0%")
    @Max(value = 100, message = "La nubosidad debe ser máximo 100%")
    private Integer cloudiness;

    private String observations;

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

}