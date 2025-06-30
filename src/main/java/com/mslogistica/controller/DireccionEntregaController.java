package com.mslogistica.controller;

import com.mslogistica.dto.CrearDireccionRequest;
import com.mslogistica.entities.DireccionEntrega;
import com.mslogistica.service.DireccionEntregaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/direcciones")
@RequiredArgsConstructor
public class DireccionEntregaController {

    private final DireccionEntregaService direccionEntregaService;

    @PostMapping
    public ResponseEntity<Void> registrarDireccion(
            @RequestHeader("X-User-Id") String userId,
            @RequestBody CrearDireccionRequest request) {

        UUID usuarioId = UUID.fromString(userId);
        direccionEntregaService.guardarDireccion(usuarioId, request);
        return ResponseEntity.ok().build();
    }

    @GetMapping
    public ResponseEntity<List<DireccionEntrega>> listarDirecciones(
            @RequestHeader("X-User-Id") String userId) {

        UUID usuarioId = UUID.fromString(userId);
        List<DireccionEntrega> direcciones = direccionEntregaService.listarDirecciones(usuarioId);
        return ResponseEntity.ok(direcciones);
    }
}
