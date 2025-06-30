package com.mslogistica.dto;

import lombok.Data;

@Data
public class CrearSedeRequest {
    private String nombre;
    private String direccion; // Opcional
    private Double latitud;   // Obligatorio si no hay direccion
    private Double longitud;  // Obligatorio si no hay direccion
    private String telefono;  // Opcional
}
