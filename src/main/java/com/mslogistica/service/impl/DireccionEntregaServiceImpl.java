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

        // ✅ 1️⃣ Intentar obtener código postal real desde lat/lng o dirección textual
        String postalCodeReal = null;

        if (request.getLatitud() != null && request.getLongitud() != null) {
            postalCodeReal = geocodingService.getPostalCodeByLatLng(
                    request.getLatitud(),
                    request.getLongitud()
            );
        } else if (request.getDireccion() != null && !request.getDireccion().isBlank()) {
            postalCodeReal = geocodingService.getPostalCodeByAddress(request.getDireccion());
        }

        // ✅ 2️⃣ Validar el postal ingresado manualmente (si existe y no está vacío)
        if (request.getCodigoPostal() != null && !request.getCodigoPostal().isBlank()) {
            if (!request.getCodigoPostal().matches("^\\d{5}$")) {
                throw new IllegalArgumentException("El código postal debe tener exactamente 5 dígitos numéricos.");
            }
        }

        // ✅ 3️⃣ Decidir el postal final, usando texto claro si no hay ninguno real
        String finalPostalCode;
        if (postalCodeReal != null && !postalCodeReal.isBlank()) {
            finalPostalCode = postalCodeReal;
        } else if (request.getCodigoPostal() != null && !request.getCodigoPostal().isBlank()) {
            finalPostalCode = request.getCodigoPostal();
        } else {
            finalPostalCode = "No hay postal";
        }

        // ✅ 4️⃣ Crear entidad lista para la entrega
        DireccionEntrega direccion = new DireccionEntrega();
        direccion.setUsuarioId(usuarioId);
        direccion.setDireccion(request.getDireccion());
        direccion.setCodigoPostal(finalPostalCode);
        direccion.setLatitud(request.getLatitud());
        direccion.setLongitud(request.getLongitud());
        direccion.setReferencia(request.getReferencia());
        direccion.setDestinatario(request.getDestinatario());

        // ✅ 5️⃣ Guardar
        direccionEntregaRepository.save(direccion);
    }

    @Override
    public List<DireccionEntrega> listarDirecciones(UUID usuarioId) {
        return direccionEntregaRepository.findByUsuarioId(usuarioId);
    }

}
