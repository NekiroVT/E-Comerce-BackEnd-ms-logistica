package com.mslogistica.service.impl;

import com.mslogistica.dto.CrearDireccionRequest;
import com.mslogistica.entities.DireccionEntrega;
import com.mslogistica.repository.DireccionEntregaRepository;
import com.mslogistica.service.DireccionEntregaService;
import com.mslogistica.service.GeocodingService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class DireccionEntregaServiceImpl implements DireccionEntregaService {

    private final DireccionEntregaRepository direccionEntregaRepository;
    private final GeocodingService geocodingService;

    @Override
    public void guardarDireccion(UUID usuarioId, CrearDireccionRequest request) {

        // ✅ 1️⃣ Obtener postal_code real usando lat/lng si existen
        String postalCodeReal = null;

        if (request.getLatitud() != null && request.getLongitud() != null) {
            postalCodeReal = geocodingService.getPostalCodeByLatLng(
                    request.getLatitud(), request.getLongitud()
            );
        } else if (request.getDireccion() != null) {
            postalCodeReal = geocodingService.getPostalCodeByAddress(request.getDireccion());
        }

        // ✅ 2️⃣ Validar formato del ZIP ingresado si existe
        if (request.getCodigoPostal() != null && !request.getCodigoPostal().matches("^\\d{5}$")) {
            throw new IllegalArgumentException("El código postal debe tener exactamente 5 dígitos numéricos.");
        }

        // ✅ 3️⃣ Decidir ZIP final
        String finalPostalCode = postalCodeReal != null ? postalCodeReal : request.getCodigoPostal();

        // ✅ 4️⃣ Crear entidad
        DireccionEntrega direccion = new DireccionEntrega();
        direccion.setUsuarioId(usuarioId);
        direccion.setDireccion(request.getDireccion());
        direccion.setCodigoPostal(finalPostalCode);
        direccion.setLatitud(request.getLatitud());
        direccion.setLongitud(request.getLongitud());
        direccion.setReferencia(request.getReferencia());
        direccion.setDestinatario(request.getDestinatario());

        direccionEntregaRepository.save(direccion);
    }

    @Override
    public List<DireccionEntrega> listarDirecciones(UUID usuarioId) {
        return direccionEntregaRepository.findByUsuarioId(usuarioId);
    }
}
