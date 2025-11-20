package com.weather.forecast.dto;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ForecastDTO {

    private Long id;

    @NotNull(message = "La fecha del pronóstico es obligatoria")
    private LocalDate forecastDate;

    private String generalObservations; // Observaciones generales del pronóstico

    private String alerts; // Alertas opcionales

    @Valid
    @NotNull(message = "Debe incluir al menos un pronóstico por zona")
    private List<ZoneForecastDTO> zoneForecasts = new ArrayList<>();

    private LocalDateTime createdAt;

    private LocalDateTime updatedAt;

    private String createdBy;
}