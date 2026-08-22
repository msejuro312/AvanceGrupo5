package com.serfagab.controller;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
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

    @GetMapping("/paginado")
    public Page<TipoMaterial> listarPaginado(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "5") int size,
            @RequestParam(defaultValue = "nombre") String criterio,
            @RequestParam(required = false) String texto) {
        Pageable pageable = PageRequest.of(page, size, Sort.by("idTipoMaterial").ascending());
        boolean hayTexto = texto != null && !texto.trim().isEmpty();
        if (hayTexto) {
            switch (criterio) {
                case "descripcion":
                    return tipoMaterialRepository.findByDescripcionContainingIgnoreCaseAndActivoTrue(texto.trim(), pageable);
                case "nombre":
                default:
                    return tipoMaterialRepository.findByNombreContainingIgnoreCaseAndActivoTrue(texto.trim(), pageable);
            }
        }
        return tipoMaterialRepository.findByActivoTrue(pageable);
    }

    @GetMapping("/{id}")
    public ResponseEntity<TipoMaterial> obtenerPorId(@PathVariable Integer id) {
        return tipoMaterialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<TipoMaterial>> buscarPorNombre(@RequestParam String texto) {
        List<TipoMaterial> resultados = tipoMaterialRepository.findByNombreContainingIgnoreCaseAndActivoTrue(texto);
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @GetMapping("/buscarPorDescripcion")
    public ResponseEntity<List<TipoMaterial>> buscarPorDescripcion(@RequestParam String texto) {
        List<TipoMaterial> resultados = tipoMaterialRepository.findByDescripcionContainingIgnoreCaseAndActivoTrue(texto);
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
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
