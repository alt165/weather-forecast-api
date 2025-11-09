package com.weather.forecast.config;

import com.weather.forecast.model.Administrator;
import com.weather.forecast.model.Zone;
import com.weather.forecast.repository.AdministratorRepository;
import com.weather.forecast.repository.ZoneRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class DataInitializer implements CommandLineRunner {

    @Autowired
    private AdministratorRepository administratorRepository;

    @Autowired
    private ZoneRepository zoneRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public void run(String... args) throws Exception {
        // Crear administrador por defecto si no existe
        if (!administratorRepository.existsByUsername("admin")) {
            Administrator admin = new Administrator();
            admin.setUsername("admin");
            admin.setPassword(passwordEncoder.encode("admin123"));
            admin.setEmail("admin@weather.com");
            admin.setEnabled(true);
            administratorRepository.save(admin);
            System.out.println("✅ Usuario administrador creado - Username: admin, Password: admin123");
        }

        // Crear zonas de ejemplo si no existen
        if (zoneRepository.count() == 0) {
            Zone zone1 = new Zone(null, "San Juan", "San Juan Capital", true);
            Zone zone2 = new Zone(null, "Iglesia", "Iglesia", true);
            Zone zone3 = new Zone(null, "Calingasta", "Calingasta y Barreal", true);
            Zone zone4 = new Zone(null, "Jáchal", "Jáchal", true);
            Zone zone5 = new Zone(null, "Valle Fértil", "Valle Fértil", true);

            zoneRepository.save(zone1);
            zoneRepository.save(zone2);
            zoneRepository.save(zone3);
            zoneRepository.save(zone4);
            zoneRepository.save(zone5);

            System.out.println("✅ Zonas de ejemplo creadas");
        }

        System.out.println("========================================");
        System.out.println("🌤️  Weather Forecast API iniciada");
        System.out.println("📍 URL: http://localhost:8080");
        System.out.println("👤 Usuario: admin");
        System.out.println("🔑 Password: admin123");
        System.out.println("========================================");
    }
}