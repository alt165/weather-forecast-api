package com.weather.forecast.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import java.time.LocalDateTime;

@Entity
@Table(name = "zone_forecasts")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class ZoneForecast {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "forecast_id", nullable = false)
    private Forecast forecast;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "zone_id", nullable = false)
    private Zone zone;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ForecastPeriod period; // ONE_DAY, THREE_DAYS, FIVE_DAYS

    // Temperatura (°C)
    @Column(nullable = false)
    private Double minTemperature;

    @Column(nullable = false)
    private Double maxTemperature;

    // Velocidad del viento (km/h)
    @Column(nullable = false)
    private Double minWindSpeed;

    @Column(nullable = false)
    private Double maxWindSpeed;

    // Precipitaciones (mm)
    @Column(nullable = false)
    private Double precipitation;

    // Nubosidad (porcentaje 0-100)
    @Column(nullable = false)
    private Integer cloudiness;

    // Observaciones particulares de esta zona
    @Lob
    @Column(columnDefinition = "TEXT")
    private String zoneObservations;

    @Column(nullable = false)
    private LocalDateTime createdAt;

    @Column(nullable = false)
    private LocalDateTime updatedAt;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
        updatedAt = LocalDateTime.now();
    }

    @PreUpdate
    protected void onUpdate() {
        updatedAt = LocalDateTime.now();
    }

    public enum ForecastPeriod {
        ONE_DAY(1),
        THREE_DAYS(3),
        FIVE_DAYS(5);

        private final int days;

        ForecastPeriod(int days) {
            this.days = days;
        }

        public int getDays() {
            return days;
        }
    }
}