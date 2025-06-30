package com.mslogistica.dto;

import lombok.Data;

@Data
public class CrearDireccionRequest {

    private String direccion;      // Opcional (texto libre)
    private String codigoPostal;   // ✅ Nuevo, opcional
    private Double latitud;        // Obligatorio
    private Double longitud;       // Obligatorio
    private String referencia;     // Opcional
    private String destinatario;   // Opcional

}
