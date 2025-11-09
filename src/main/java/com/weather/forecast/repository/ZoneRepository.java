package com.weather.forecast.repository;

import com.weather.forecast.model.Zone;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;

@Repository
public interface ZoneRepository extends JpaRepository<Zone, Long> {
    Optional<Zone> findByName(String name);
    List<Zone> findByActiveTrue();
    boolean existsByName(String name);
}