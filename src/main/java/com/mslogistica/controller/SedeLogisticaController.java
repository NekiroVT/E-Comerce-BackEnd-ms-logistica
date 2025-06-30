package com.mslogistica.controller;

import com.mslogistica.dto.CrearSedeRequest;
import com.mslogistica.entities.SedeLogistica;
import com.mslogistica.service.SedeLogisticaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/sedes")
@RequiredArgsConstructor
public class SedeLogisticaController {

    private final SedeLogisticaService sedeLogisticaService;

    @PostMapping
    public ResponseEntity<?> crearSede(@RequestBody CrearSedeRequest request) {
        sedeLogisticaService.crearSede(request);
        return ResponseEntity.ok().body(
                "✅ Sede creada correctamente"
        );
    }

    @GetMapping
    public ResponseEntity<List<SedeLogistica>> listarSedes() {
        return ResponseEntity.ok(sedeLogisticaService.listarSedes());
    }


}
