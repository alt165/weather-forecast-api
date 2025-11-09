package com.weather.forecast.service;

import com.weather.forecast.dto.ZoneDTO;
import com.weather.forecast.model.Zone;
import com.weather.forecast.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class ZoneService {

    @Autowired
    private ZoneRepository zoneRepository;

    @Transactional
    public ZoneDTO createZone(ZoneDTO zoneDTO) {
        if (zoneRepository.existsByName(zoneDTO.getName())) {
            throw new RuntimeException("Ya existe una zona con ese nombre");
        }

        Zone zone = new Zone();
        zone.setName(zoneDTO.getName());
        zone.setDescription(zoneDTO.getDescription());
        zone.setActive(zoneDTO.isActive());

        Zone savedZone = zoneRepository.save(zone);
        return convertToDTO(savedZone);
    }

    @Transactional
    public ZoneDTO updateZone(Long id, ZoneDTO zoneDTO) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));

        if (!zone.getName().equals(zoneDTO.getName()) &&
                zoneRepository.existsByName(zoneDTO.getName())) {
            throw new RuntimeException("Ya existe una zona con ese nombre");
        }

        zone.setName(zoneDTO.getName());
        zone.setDescription(zoneDTO.getDescription());
        zone.setActive(zoneDTO.isActive());

        Zone updatedZone = zoneRepository.save(zone);
        return convertToDTO(updatedZone);
    }

    @Transactional(readOnly = true)
    public ZoneDTO getZoneById(Long id) {
        Zone zone = zoneRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Zona no encontrada"));
        return convertToDTO(zone);
    }

    @Transactional(readOnly = true)
    public List<ZoneDTO> getAllZones() {
        return zoneRepository.findAll().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional(readOnly = true)
    public List<ZoneDTO> getActiveZones() {
        return zoneRepository.findByActiveTrue().stream()
                .map(this::convertToDTO)
                .collect(Collectors.toList());
    }

    @Transactional
    public void deleteZone(Long id) {
        if (!zoneRepository.existsById(id)) {
            throw new RuntimeException("Zona no encontrada");
        }
        zoneRepository.deleteById(id);
    }

    private ZoneDTO convertToDTO(Zone zone) {
        ZoneDTO dto = new ZoneDTO();
        dto.setId(zone.getId());
        dto.setName(zone.getName());
        dto.setDescription(zone.getDescription());
        dto.setActive(zone.isActive());
        return dto;
    }
}