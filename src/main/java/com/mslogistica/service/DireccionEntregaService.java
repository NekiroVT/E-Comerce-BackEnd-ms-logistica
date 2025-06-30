package com.mslogistica.service;

import com.mslogistica.dto.CrearDireccionRequest;

import java.util.List;
import java.util.UUID;

public interface DireccionEntregaService {
    void guardarDireccion(UUID usuarioId, CrearDireccionRequest request);

    List<com.mslogistica.entities.DireccionEntrega> listarDirecciones(UUID usuarioId);
}
