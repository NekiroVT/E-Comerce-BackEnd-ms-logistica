package com.mslogistica.client;

import com.mslogistica.dto.CambiarEstadoProductoRequest;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.UUID;

@FeignClient(name = "ms-productos")
public interface ProductoClient {

    @PutMapping("/api/productos/{id}/estado")
    void actualizarEstadoProducto(
            @PathVariable("id") UUID productoId,
            @RequestBody CambiarEstadoProductoRequest request
    );


}
