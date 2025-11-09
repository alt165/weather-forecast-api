package com.weather.forecast.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneDTO {

    private Long id;

    @NotBlank(message = "El nombre de la zona es obligatorio")
    private String name;

    private String description;

    private boolean active = true;
}