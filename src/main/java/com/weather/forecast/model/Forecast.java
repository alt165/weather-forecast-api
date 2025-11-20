package com.weather.forecast.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "forecasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Forecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private LocalDate forecastDate;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String generalObservations;

    @Lob
    @Column(columnDefinition = "TEXT")
    private String alerts; // Campo opcional para alertas

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "created_by", nullable = false)
    private Administrator createdBy;

    @OneToMany(mappedBy = "forecast", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ZoneForecast> zoneForecasts = new ArrayList<>();

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    // Método helper para agregar un pronóstico de zona
    public void addZoneForecast(ZoneForecast zoneForecast) {
        zoneForecasts.add(zoneForecast);
        zoneForecast.setForecast(this);
    }

    // Método helper para remover un pronóstico de zona
    public void removeZoneForecast(ZoneForecast zoneForecast) {
        zoneForecasts.remove(zoneForecast);
        zoneForecast.setForecast(null);
    }
}