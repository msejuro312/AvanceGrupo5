package com.serfagab.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serfagab.entities.Proveedor;
import com.serfagab.repository.ProveedorRepository;
import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {
    private final ProveedorRepository proveedorRepository;

    public ProveedorController(ProveedorRepository proveedorRepository) {
        this.proveedorRepository = proveedorRepository;
    }

    @PostMapping
    public ResponseEntity<Proveedor> crear(@RequestBody Proveedor proveedor) {
        proveedor.setIdProveedor(null);
        return ResponseEntity.ok(proveedorRepository.save(proveedor));
    }

    @GetMapping
    public List<Proveedor> listar() {
        return proveedorRepository.findByActivoTrue();
    }

    @GetMapping("/paginado")
    public Page<Proveedor> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "razonSocial") String criterio,
            @RequestParam(required = false) String texto) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idProveedor").ascending());
        boolean hayTexto = texto != null && !texto.trim().isEmpty();
        if (hayTexto) {
            switch (criterio) {
                case "ruc":
                    return proveedorRepository.findByRucContainingAndActivoTrue(texto.trim(), pageable);
                case "email":
                    return proveedorRepository.findByEmailContainingIgnoreCaseAndActivoTrue(texto.trim(), pageable);
                case "razonSocial":
                default:
                    return proveedorRepository.findByRazonSocialContainingIgnoreCaseAndActivoTrue(texto.trim(), pageable);
            }
        }
        return proveedorRepository.findByActivoTrue(pageable);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Proveedor> eliminar(@PathVariable Integer id) {
        return proveedorRepository.findById(id)
                .map(p -> {
                    p.setActivo(false);
                    return ResponseEntity.ok(proveedorRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<Proveedor> obtenerPorId(@PathVariable Integer id) {
        return proveedorRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarPorRazonSocial")
    public ResponseEntity<List<Proveedor>> buscarPorRazonSocial(@RequestParam String texto) {
        List<Proveedor> resultados = proveedorRepository.findByRazonSocialContainingIgnoreCaseAndActivoTrue(texto);
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscarPorRuc")
    public ResponseEntity<List<Proveedor>> buscarPorRuc(@RequestParam String texto) {
        List<Proveedor> resultados = proveedorRepository.findByRucContainingAndActivoTrue(texto);
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscarPorEmail")
    public ResponseEntity<List<Proveedor>> buscarPorEmail(@RequestParam String texto) {
        List<Proveedor> resultados = proveedorRepository.findByEmailContainingIgnoreCaseAndActivoTrue(texto);
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Proveedor> actualizar(@PathVariable Integer id, @RequestBody Proveedor proveedor) {
        return proveedorRepository.findById(id)
                .map(p -> {
                    p.setRazonSocial(proveedor.getRazonSocial());
                    p.setRuc(proveedor.getRuc());
                    p.setCelular(proveedor.getCelular());
                    p.setEmail(proveedor.getEmail());
                    p.setDescripcion(proveedor.getDescripcion());
                    p.setActivo(proveedor.getActivo());
                    return ResponseEntity.ok(proveedorRepository.save(p));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
