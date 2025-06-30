package com.mslogistica.service;

import com.mslogistica.dto.CalcularEnvioRequest;
import com.mslogistica.dto.EnvioPreviewResponse;
import com.mslogistica.dto.RegistroLogisticaRequest;

import java.util.UUID;

public interface ProductoLogisticaService {

    void registrarProducto(RegistroLogisticaRequest request);

    void aceptarProducto(UUID productoId, UUID sedeId);

    void rechazarProducto(UUID productoId, UUID sedeId, String motivo);

    void cancelarProducto(UUID productoId, String motivo);

    EnvioPreviewResponse calcularEnvio(CalcularEnvioRequest request);


}
