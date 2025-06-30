package com.mslogistica.repository;

import com.mslogistica.entities.DireccionEntrega;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.UUID;

public interface DireccionEntregaRepository extends JpaRepository<DireccionEntrega, UUID> {
    List<DireccionEntrega> findByUsuarioId(UUID usuarioId);
}
