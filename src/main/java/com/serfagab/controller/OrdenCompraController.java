package com.serfagab.controller;

import com.serfagab.entities.Material;
import com.serfagab.repository.MaterialRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serfagab.entities.DetalleOrdenCompra;
import com.serfagab.entities.OrdenCompra;
import com.serfagab.repository.OrdenCompraRepository;
import com.serfagab.service.OrdenCompraService;
import com.serfagab.util.OrdenCompraPdf;
import java.time.LocalDate;
import java.util.List;

@RestController
@RequestMapping("/api/ordenes-compra")
public class OrdenCompraController {
    private final OrdenCompraService ordenCompraService;
    private final OrdenCompraRepository ordenCompraRepository;
    private final MaterialRepository materialRepository;

    public OrdenCompraController(OrdenCompraService ordenCompraService, OrdenCompraRepository ordenCompraRepository, MaterialRepository materialRepository) {
        this.ordenCompraService = ordenCompraService;
        this.ordenCompraRepository = ordenCompraRepository;
        this.materialRepository = materialRepository;
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
        if (detalle.getCantidad() == null || detalle.getCantidad() <=0) {
            return ResponseEntity.badRequest().body("La cantidad debe ser mayor a 0");
        }
        if (detalle.getPrecioUnitario() == null || detalle.getPrecioUnitario() <=0) {
            return ResponseEntity.badRequest().body("El precio unitario debe ser mayor a 0");
        }

        Material material = materialRepository.findById(detalle.getMaterial().getIdMaterial()).orElse(null);
        if (material == null) {
            return ResponseEntity.badRequest().body("El material no existe");
        }

        double referencial = material.getPrecioReferencial();
        double minimo = referencial * 0.5;
        double maximo = referencial * 2.0;

        if (detalle.getPrecioUnitario() < minimo || detalle.getPrecioUnitario() > maximo) {
            return ResponseEntity.badRequest().body(
                    "El precio se aleja demasiado del referencial (S/ "+ referencial + ")");
        }

        DetalleOrdenCompra guardado = ordenCompraService.agregarDetalle(
                idOrden,
                detalle.getMaterial().getIdMaterial(),
                detalle.getCantidad(),
                detalle.getPrecioUnitario());
        return ResponseEntity.ok(guardado);
    }

    @PutMapping("/{idOrden}")
    public ResponseEntity<?> actualizar(@PathVariable Integer idOrden, @RequestBody OrdenCompra datos) {
        if (datos.getProveedor() == null || datos.getProveedor().getIdProveedor() == null) {
            return ResponseEntity.badRequest().body("Debe especificar un proveedor");
        }
        try {
            OrdenCompra actualizada = ordenCompraService.actualizarOrden(
                    idOrden,
                    datos.getProveedor().getIdProveedor(),
                    datos.getFecha(),
                    datos.getObservaciones());
            return ResponseEntity.ok(actualizada);
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }

    /*
    @DeleteMapping("/{idOrden}")
    public ResponseEntity<?> eliminar(@PathVariable Integer idOrden) {
        if (!ordenCompraRepository.existsById(idOrden)) {
            return ResponseEntity.notFound().build();
        }
        ordenCompraService.eliminarOrden(idOrden);
        return ResponseEntity.noContent().build();
    }
    */

    @PutMapping("/{idOrden}/estado")
    public ResponseEntity<?> cambiarEstado(@PathVariable Integer idOrden, @RequestParam String estado) {
        return ordenCompraRepository.findById(idOrden)
                .map(orden -> {
                    if ("ENVIADO".equals(orden.getEstado()) || "ANULADO".equals(orden.getEstado())){
                        return ResponseEntity.badRequest()
                                .body("No se puede cambiar el estado de una orden ya " + orden.getEstado().toLowerCase());
                    }
                    if ("ENVIADO".equals(estado)) {
                        for (DetalleOrdenCompra detalle : orden.getDetalles()) {
                            Material material = detalle.getMaterial();
                            material.setStockActual(material.getStockActual() + detalle.getCantidad());
                            materialRepository.save(material);
                        }
                    }
                    orden.setEstado(estado);
                    return ResponseEntity.ok(ordenCompraRepository.save(orden));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/usuario/{idUsuario}")
    public List<OrdenCompra> historial(@PathVariable Integer idUsuario) {
        return ordenCompraService.listarPorUsuario(idUsuario);
    }

    @GetMapping("/usuario/{idUsuario}/paginado")
    public Page<OrdenCompra> historialPaginado(
            @PathVariable Integer idUsuario,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(required = false) String estado,
            @RequestParam(required = false) Integer idProveedor,
            @RequestParam(required = false) LocalDate fechaDesde,
            @RequestParam(required = false) LocalDate fechaHasta) {
        Sort sort = Sort.by(Sort.Order.desc("fecha"), Sort.Order.desc("idOrdenCompra"));
        Pageable pageable = PageRequest.of(page, size, sort);
        return ordenCompraRepository.buscarPaginadoConFiltros(idUsuario, estado, idProveedor, fechaDesde, fechaHasta, pageable);
    }

    @GetMapping("/usuario/{idUsuario}/paginado/ordenado")
    public Page<OrdenCompra> historialPaginadoOrdenado(
            @PathVariable Integer idUsuario,
            @RequestParam int page,
            @RequestParam int size,
            @RequestParam(defaultValue = "fecha") String sortBy,
            @RequestParam(defaultValue = "desc") String order
    ) {
        Sort sort = order.equalsIgnoreCase("asc") ?
                Sort.by(sortBy).ascending() :
                Sort.by(sortBy).descending();
        Pageable pageable = PageRequest.of(page, size, sort);
        return ordenCompraRepository.findByUsuarioIdUsuario(idUsuario, pageable);
    }

    @GetMapping("/estado")
    public ResponseEntity<List<OrdenCompra>> listarPorEstado(@RequestParam String estado) {
        List<OrdenCompra> ordenes = ordenCompraRepository.findByEstado(estado);
        if (ordenes.isEmpty()) {
            return ResponseEntity.noContent().build();
        }
        return ResponseEntity.ok(ordenes);
    }

    @GetMapping("/{idOrden}")
    public ResponseEntity<OrdenCompra> detalle(@PathVariable Integer idOrden) {
        return ordenCompraRepository.findById(idOrden)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{idOrden}/pdf")
    public ResponseEntity<byte[]> descargarPdf(@PathVariable Integer idOrden) {
        return ordenCompraRepository.findById(idOrden)
                .map(orden -> {
                    byte[] pdf = OrdenCompraPdf.generar(orden);
                    return ResponseEntity.ok()
                            .contentType(MediaType.APPLICATION_PDF)
                            .header(HttpHeaders.CONTENT_DISPOSITION,
                                    "attachment; filename=orden-compra-" + idOrden + ".pdf")
                            .body(pdf);
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
