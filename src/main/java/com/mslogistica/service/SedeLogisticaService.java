package com.mslogistica.service;

import com.mslogistica.dto.CrearSedeRequest;
import com.mslogistica.entities.SedeLogistica;

import java.util.List;

public interface SedeLogisticaService {
    void crearSede(CrearSedeRequest request);
    List<SedeLogistica> listarSedes();
}
