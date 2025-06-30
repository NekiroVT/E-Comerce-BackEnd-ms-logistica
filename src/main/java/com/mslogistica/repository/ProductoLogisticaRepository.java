package com.mslogistica.repository;

import com.mslogistica.entities.ProductoLogistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.UUID;

public interface ProductoLogisticaRepository extends JpaRepository<ProductoLogistica, UUID> {
    Optional<ProductoLogistica> findByProductoId(UUID productoId);
}
