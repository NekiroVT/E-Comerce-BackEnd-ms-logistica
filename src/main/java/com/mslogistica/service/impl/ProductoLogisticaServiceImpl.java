package com.mslogistica.service.impl;

import com.mslogistica.client.ProductoClient;
import com.mslogistica.dto.CalcularEnvioRequest;
import com.mslogistica.dto.CambiarEstadoProductoRequest;
import com.mslogistica.dto.EnvioPreviewResponse;
import com.mslogistica.dto.RegistroLogisticaRequest;
import com.mslogistica.entities.DireccionEntrega;
import com.mslogistica.entities.ProductoLogistica;
import com.mslogistica.entities.SedeLogistica;
import com.mslogistica.enums.EstadoLogistico;
import com.mslogistica.repository.DireccionEntregaRepository;
import com.mslogistica.repository.ProductoLogisticaRepository;
import com.mslogistica.repository.SedeLogisticaRepository;
import com.mslogistica.service.ProductoLogisticaService;
import com.mslogistica.util.DistanceUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.*;

@Service
@RequiredArgsConstructor
public class ProductoLogisticaServiceImpl implements ProductoLogisticaService {

    private final ProductoLogisticaRepository productoLogisticaRepository;
    private final SedeLogisticaRepository sedeLogisticaRepository; // ✅ NUEVO: para obtener la sede
    private final ProductoClient productoClient;
    private final DireccionEntregaRepository direccionEntregaRepository;


    @Override
    public void registrarProducto(RegistroLogisticaRequest request) {
        ProductoLogistica registro = ProductoLogistica.builder()
                .productoId(request.getProductoId())
                .usuarioId(request.getUsuarioId())
                .estado(EstadoLogistico.PENDIENTE)
                .fechaRegistro(LocalDateTime.now())
                .build();

        productoLogisticaRepository.save(registro);
    }

    @Override
    public void aceptarProducto(UUID productoId, UUID sedeId) {
        ProductoLogistica registro = productoLogisticaRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No existe registro logístico"));

        SedeLogistica sede = sedeLogisticaRepository.findById(sedeId)
                .orElseThrow(() -> new RuntimeException("No existe sede logística"));

        registro.setEstado(EstadoLogistico.ACEPTADO);
        registro.setSede(sede); // ✅ Asigna la sede donde se aceptó
        registro.setMotivo("Su producto fue aceptado");
        registro.setFechaRevision(LocalDateTime.now());
        productoLogisticaRepository.save(registro);

        CambiarEstadoProductoRequest request = new CambiarEstadoProductoRequest();
        request.setNuevoEstado("ACTIVO");
        productoClient.actualizarEstadoProducto(productoId, request);
    }

    @Override
    public void rechazarProducto(UUID productoId, UUID sedeId, String motivo) {
        ProductoLogistica registro = productoLogisticaRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No existe registro logístico"));

        SedeLogistica sede = sedeLogisticaRepository.findById(sedeId)
                .orElseThrow(() -> new RuntimeException("No existe sede logística"));

        registro.setEstado(EstadoLogistico.RECHAZADO);
        registro.setSede(sede); // ✅ También se guarda la sede que rechazó
        registro.setMotivo(motivo);
        registro.setFechaRevision(LocalDateTime.now());
        productoLogisticaRepository.save(registro);

        CambiarEstadoProductoRequest request = new CambiarEstadoProductoRequest();
        request.setNuevoEstado("RECHAZADO");
        productoClient.actualizarEstadoProducto(productoId, request);
    }

    @Override
    public void cancelarProducto(UUID productoId, String motivo) {
        ProductoLogistica registro = productoLogisticaRepository.findByProductoId(productoId)
                .orElseThrow(() -> new RuntimeException("No existe registro logístico"));

        // Si estaba PENDIENTE → limpiar sede
        if (registro.getEstado() == EstadoLogistico.PENDIENTE) {
            registro.setSede(null);
        }
        // Si estaba ACEPTADO o RECHAZADO → se mantiene la sede

        registro.setEstado(EstadoLogistico.CANCELADO);
        registro.setMotivo(motivo);
        registro.setFechaRevision(LocalDateTime.now());
        productoLogisticaRepository.save(registro);

        CambiarEstadoProductoRequest request = new CambiarEstadoProductoRequest();
        request.setNuevoEstado("CANCELADO");
        productoClient.actualizarEstadoProducto(productoId, request);
    }

    @Override
    public EnvioPreviewResponse calcularEnvio(CalcularEnvioRequest request) {
        // 1️⃣ Busca la dirección de entrega seleccionada
        DireccionEntrega direccion = direccionEntregaRepository.findById(request.getDireccionId())
                .orElseThrow(() -> new RuntimeException("Dirección no encontrada"));

        // 2️⃣ Agrupa productos por sede logística
        Map<UUID, List<UUID>> sedeAgrupada = new HashMap<>();

        for (UUID productoId : request.getProductoIds()) {
            ProductoLogistica productoLogistica = productoLogisticaRepository.findByProductoId(productoId)
                    .orElseThrow(() -> new RuntimeException("Producto logístico no encontrado"));

            if (productoLogistica.getSede() == null) {
                throw new RuntimeException("El producto no tiene sede asignada");
            }

            UUID sedeId = productoLogistica.getSede().getId();
            sedeAgrupada.computeIfAbsent(sedeId, k -> new ArrayList<>()).add(productoId);
        }

        List<EnvioPreviewResponse.EnvioCalculado> envios = new ArrayList<>();

        // 3️⃣ Para cada sede → calcula distancia, precio y tiempo estimado
        for (UUID sedeId : sedeAgrupada.keySet()) {
            SedeLogistica sede = sedeLogisticaRepository.findById(sedeId)
                    .orElseThrow(() -> new RuntimeException("Sede logística no encontrada"));

            double distanciaKm = DistanceUtil.haversine(
                    sede.getLatitud(), sede.getLongitud(),
                    direccion.getLatitud(), direccion.getLongitud()
            );

            double precioEnvio;
            int tiempoDias;

            if (distanciaKm <= 5) {
                precioEnvio = 5.0;
                tiempoDias = 1;
            } else if (distanciaKm <= 10) {
                precioEnvio = 10.0;
                tiempoDias = 2;
            } else if (distanciaKm <= 30) {
                precioEnvio = 20.0;
                tiempoDias = 3;
            } else {
                precioEnvio = 30.0; // Precio máximo
                tiempoDias = 5;
            }

            EnvioPreviewResponse.EnvioCalculado calculado = new EnvioPreviewResponse.EnvioCalculado();
            calculado.setSedeId(sedeId);
            calculado.setNombreSede(sede.getNombre());
            calculado.setProductoIds(sedeAgrupada.get(sedeId));
            calculado.setDistanciaKm(distanciaKm);
            calculado.setPrecioEnvio(precioEnvio);
            calculado.setTiempoEstimadoDias(tiempoDias);

            envios.add(calculado);
        }

        EnvioPreviewResponse response = new EnvioPreviewResponse();
        response.setEnvios(envios);
        return response;
    }




}
