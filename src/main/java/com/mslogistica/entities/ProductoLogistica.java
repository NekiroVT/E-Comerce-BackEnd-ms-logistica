package com.mslogistica.entities;

import com.mslogistica.enums.EstadoLogistico;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDateTime;
import java.util.UUID;

@Entity
@Table(name = "producto_logistica")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoLogistica {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "id_logistica")
    private UUID id;

    @Column(name = "producto_id", nullable = false, columnDefinition = "RAW(16)")
    private UUID productoId;

    @Column(name = "usuario_id", nullable = false, columnDefinition = "RAW(16)")
    private UUID usuarioId;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private EstadoLogistico estado = EstadoLogistico.PENDIENTE;

    @Column(length = 500)
    private String motivo;

    @Column(name = "fecha_registro", nullable = false)
    private LocalDateTime fechaRegistro = LocalDateTime.now();

    @Column(name = "fecha_revision")
    private LocalDateTime fechaRevision;

    /**
     * ✅ Sede asignada donde se revisa/acepta/rechaza este producto
     */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sede_id") // FK: sede_id
    private SedeLogistica sede;
}
