package com.mslogistica.service.impl;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.mslogistica.dto.CrearSedeRequest;
import com.mslogistica.entities.SedeLogistica;
import com.mslogistica.repository.SedeLogisticaRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.List;

@Service
@RequiredArgsConstructor
public class SedeLogisticaServiceImpl implements com.mslogistica.service.SedeLogisticaService {

    private final SedeLogisticaRepository sedeLogisticaRepository;

    @Value("${google.maps.api.key}")
    private String apiKey;

    private final RestTemplate restTemplate = new RestTemplate();
    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public void crearSede(CrearSedeRequest request) {
        double lat, lon;

        if (request.getLatitud() != null && request.getLongitud() != null) {
            // Ya vinieron coordenadas: se usan tal cual.
            lat = request.getLatitud();
            lon = request.getLongitud();
        } else if (request.getDireccion() != null && !request.getDireccion().isBlank()) {
            // Si no hay coordenadas pero sí dirección → geocodificar.
            String url = "https://maps.googleapis.com/maps/api/geocode/json?address=" +
                    URLEncoder.encode(request.getDireccion(), StandardCharsets.UTF_8) +
                    "&key=" + apiKey;

            String response = restTemplate.getForObject(url, String.class);

            try {
                JsonNode root = objectMapper.readTree(response);
                String status = root.path("status").asText();
                if (!"OK".equals(status)) {
                    throw new RuntimeException("❌ Dirección no válida o no encontrada");
                }
                JsonNode location = root.path("results").get(0).path("geometry").path("location");
                lat = location.path("lat").asDouble();
                lon = location.path("lng").asDouble();
            } catch (Exception e) {
                throw new RuntimeException("❌ Error al geocodificar la dirección");
            }
        } else {
            throw new RuntimeException("❌ Debes enviar coordenadas o dirección");
        }

        // Guardar sede:
        SedeLogistica sede = SedeLogistica.builder()
                .nombre(request.getNombre())
                .direccion(request.getDireccion())
                .latitud(lat)
                .longitud(lon)
                .telefono(request.getTelefono())
                .build();

        sedeLogisticaRepository.save(sede);
    }

    @Override
    public List<SedeLogistica> listarSedes() {
        return sedeLogisticaRepository.findAll();
    }

}
