package com.mslogistica.dto;

import lombok.Data;
import java.util.UUID;

@Data
public class RegistroLogisticaRequest {
    private UUID productoId;
    private UUID usuarioId;
}
