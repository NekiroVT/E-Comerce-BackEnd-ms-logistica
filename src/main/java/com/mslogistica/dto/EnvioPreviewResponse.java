package com.mslogistica.dto;

import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class EnvioPreviewResponse {
    private List<EnvioCalculado> envios;

    @Data
    public static class EnvioCalculado {
        private UUID sedeId;
        private String nombreSede;
        private List<UUID> productoIds;
        private double distanciaKm;
        private double precioEnvio;
        private int tiempoEstimadoDias;
    }
}
