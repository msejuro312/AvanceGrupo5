package com.serfagab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serfagab.entities.DetalleOrdenCompra;
import com.serfagab.entities.OrdenCompra;
import com.serfagab.repository.OrdenCompraRepository;
import com.serfagab.service.OrdenCompraService;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes-compra")
public class OrdenCompraController {
    private final OrdenCompraService ordenCompraService;
    private final OrdenCompraRepository ordenCompraRepository;

    public OrdenCompraController(OrdenCompraService ordenCompraService, OrdenCompraRepository ordenCompraRepository) {
        this.ordenCompraService = ordenCompraService;
        this.ordenCompraRepository = ordenCompraRepository;
    }

    @PostMapping("/{idUsuario}/crear")
    public ResponseEntity<?> crear(@PathVariable Integer idUsuario, @RequestBody OrdenCompra ordenCompra) {
        if (ordenCompra.getProveedor() == null || ordenCompra.getProveedor().getIdProveedor() == null) {
            return ResponseEntity.badRequest().body("Debe especificar un proveedor");
        }
        OrdenCompra creada = ordenCompraService.crearOrden(
                idUsuario,
                ordenCompra.getProveedor().getIdProveedor(),
                ordenCompra.getFecha(),
                ordenCompra.getObservaciones());
        return ResponseEntity.ok(creada);
    }

    @PostMapping("/{idOrden}/agregar-detalle")
    public ResponseEntity<?> agregarDetalle(@PathVariable Integer idOrden, @RequestBody DetalleOrdenCompra detalle) {
        if (detalle.getMaterial() == null || detalle.getMaterial().getIdMaterial() == null) {
            return ResponseEntity.badRequest().body("Debe especificar un material");
        }
        DetalleOrdenCompra guardado = ordenCompraService.agregarDetalle(
                idOrden,
                detalle.getMaterial().getIdMaterial(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{idOrden}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer idOrden, @RequestParam String estado) {
        return ordenCompraRepository.findById(idOrden)
                .map(orden -> {
                    orden.setEstado(estado);
                    return ResponseEntity.ok(ordenCompraRepository.save(orden));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<OrdenCompra> historial(@PathVariable Integer idUsuario) {
        return ordenCompraService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/{idOrden}")
    public ResponseEntity<OrdenCompra> detalle(@PathVariable Integer idOrden) {
        return ordenCompraRepository.findById(idOrden)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }
}
