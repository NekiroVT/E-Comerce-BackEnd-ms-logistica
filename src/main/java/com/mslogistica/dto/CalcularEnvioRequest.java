package com.mslogistica.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import lombok.Data;

import java.util.List;
import java.util.UUID;

@Data
public class CalcularEnvioRequest {

    @JsonProperty("producto-ids") // 👈 Esto mapea el campo del JSON con guiones
    private List<UUID> productoIds;

    @JsonProperty("direccion-id") // 👈 Igual para direccion-id
    private UUID direccionId;

}
