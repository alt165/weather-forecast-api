package com.weather.forecast.controller;

import com.weather.forecast.dto.ZoneDTO;
import com.weather.forecast.service.ZoneService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping("/api/zones")
@CrossOrigin(origins = "*", maxAge = 3600)
public class ZoneController {

    @Autowired
    private ZoneService zoneService;

    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDTO> createZone(@Valid @RequestBody ZoneDTO zoneDTO) {
        try {
            ZoneDTO createdZone = zoneService.createZone(zoneDTO);
            return ResponseEntity.status(HttpStatus.CREATED).body(createdZone);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDTO> updateZone(@PathVariable Long id, @Valid @RequestBody ZoneDTO zoneDTO) {
        try {
            ZoneDTO updatedZone = zoneService.updateZone(id, zoneDTO);
            return ResponseEntity.ok(updatedZone);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().build();
        }
    }

    @GetMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<ZoneDTO> getZoneById(@PathVariable Long id) {
        try {
            ZoneDTO zone = zoneService.getZoneById(id);
            return ResponseEntity.ok(zone);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<List<ZoneDTO>> getAllZones() {
        List<ZoneDTO> zones = zoneService.getAllZones();
        return ResponseEntity.ok(zones);
    }

    @GetMapping("/active")
    public ResponseEntity<List<ZoneDTO>> getActiveZones() {
        List<ZoneDTO> zones = zoneService.getActiveZones();
        return ResponseEntity.ok(zones);
    }

    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> deleteZone(@PathVariable Long id) {
        try {
            zoneService.deleteZone(id);
            return ResponseEntity.noContent().build();
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }
}