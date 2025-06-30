package com.mslogistica.controller;

import com.mslogistica.dto.*;
import com.mslogistica.service.ProductoLogisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/producto-logistica")
@RequiredArgsConstructor
public class ProductoLogisticaController {

    private final ProductoLogisticaService productoLogisticaService;

    @PostMapping("/registrar")
    public ResponseEntity<Void> registrarProducto(@RequestBody RegistroLogisticaRequest request) {
        productoLogisticaService.registrarProducto(request);
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productoId}/aceptar")
    public ResponseEntity<Void> aceptarProducto(
            @PathVariable UUID productoId,
            @RequestBody AceptarProductoRequest request) {
        productoLogisticaService.aceptarProducto(productoId, request.getSedeId());
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productoId}/rechazar")
    public ResponseEntity<Void> rechazarProducto(
            @PathVariable UUID productoId,
            @RequestBody RechazarProductoRequest request) {
        productoLogisticaService.rechazarProducto(
                productoId,
                request.getSedeId(),
                request.getMotivo()
        );
        return ResponseEntity.ok().build();
    }

    @PutMapping("/{productoId}/cancelar")
    public ResponseEntity<Void> cancelarProducto(
            @PathVariable UUID productoId,
            @RequestBody MotivoCancelacionRequest request) {
        productoLogisticaService.cancelarProducto(productoId, request.getMotivo());
        return ResponseEntity.ok().build();
    }

    @PostMapping("/calcular-envio")
    public ResponseEntity<EnvioPreviewResponse> calcularEnvio(@RequestBody CalcularEnvioRequest request) {
        EnvioPreviewResponse preview = productoLogisticaService.calcularEnvio(request);
        return ResponseEntity.ok(preview);
    }






}
