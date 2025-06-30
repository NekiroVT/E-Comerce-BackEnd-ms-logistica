package com.mslogistica.repository;

import com.mslogistica.entities.SedeLogistica;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.UUID;

public interface SedeLogisticaRepository extends JpaRepository<SedeLogistica, UUID> {
}
