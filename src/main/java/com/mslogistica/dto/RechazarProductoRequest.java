package com.mslogistica.dto;

import lombok.Data;

import java.util.UUID;

@Data
public class RechazarProductoRequest {
    private UUID sedeId;
    private String motivo;
}
