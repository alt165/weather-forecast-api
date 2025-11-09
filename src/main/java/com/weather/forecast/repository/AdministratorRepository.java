package com.weather.forecast.repository;

import com.weather.forecast.model.Administrator;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.util.Optional;

@Repository
public interface AdministratorRepository extends JpaRepository<Administrator, Long> {
    Optional<Administrator> findByUsername(String username);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}