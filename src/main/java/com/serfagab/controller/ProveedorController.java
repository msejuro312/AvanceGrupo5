package com.serfagab.controller;

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
