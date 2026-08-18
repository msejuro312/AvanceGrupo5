package com.serfagab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serfagab.entities.TipoMaterial;
import com.serfagab.repository.TipoMaterialRepository;
import java.util.List;

@RestController
@RequestMapping("/api/tipos-material")
public class TipoMaterialController {
    private final TipoMaterialRepository tipoMaterialRepository;

    public TipoMaterialController(TipoMaterialRepository tipoMaterialRepository) {
        this.tipoMaterialRepository = tipoMaterialRepository;
    }

    @PostMapping
    public ResponseEntity<TipoMaterial> crear(@RequestBody TipoMaterial tipoMaterial) {
        tipoMaterial.setIdTipoMaterial(null);
        return ResponseEntity.ok(tipoMaterialRepository.save(tipoMaterial));
    }

    @GetMapping
    public List<TipoMaterial> listar() {
        return tipoMaterialRepository.findByActivoTrue();
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoMaterial> obtenerPorId(@PathVariable Integer id) {
        return tipoMaterialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<TipoMaterial> actualizar(@PathVariable Integer id, @RequestBody TipoMaterial tipoMaterial) {
        return tipoMaterialRepository.findById(id)
                .map(t -> {
                    t.setNombre(tipoMaterial.getNombre());
                    t.setDescripcion(tipoMaterial.getDescripcion());
                    return ResponseEntity.ok(tipoMaterialRepository.save(t));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<TipoMaterial> eliminar(@PathVariable Integer id) {
        return tipoMaterialRepository.findById(id)
                .map(t -> {
                    t.setActivo(false);
                    return ResponseEntity.ok(tipoMaterialRepository.save(t));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
