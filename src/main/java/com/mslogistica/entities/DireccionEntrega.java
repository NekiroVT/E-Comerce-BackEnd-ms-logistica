package com.mslogistica.entities;

import jakarta.persistence.*;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "direccion_entrega")
@Data
@NoArgsConstructor
public class DireccionEntrega {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    private UUID id;

    @Column(nullable = false, columnDefinition = "RAW(16)")
    private UUID usuarioId;

    private String direccion;

    private String codigoPostal; // ✅ Nuevo campo OPCIONAL

    @Column(nullable = false)
    private Double latitud;

    @Column(nullable = false)
    private Double longitud;

    private String referencia;

    private String destinatario;

    @Column(nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();
}
