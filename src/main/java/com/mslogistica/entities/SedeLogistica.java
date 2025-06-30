package com.mslogistica.entities;

import jakarta.persistence.*;
import lombok.*;
import java.util.UUID;

@Entity
@Table(name = "sede_logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SedeLogistica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_sede")
    private UUID id;

    @Column(nullable = false, length = 100)
    private String nombre;

    @Column(length = 255)
    private String direccion;  // Opcional, puede quedar NULL

    @Column(nullable = false)
    private Double latitud;    // Siempre requerido

    @Column(nullable = false)
    private Double longitud;   // Siempre requerido

    @Column(length = 20)
    private String telefono;   // Opcional
}
