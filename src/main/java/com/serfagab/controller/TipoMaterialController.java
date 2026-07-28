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
        return ResponseEntity.ok(tipoMaterialRepository.save(tipoMaterial));
    }

    @GetMapping
    public List<TipoMaterial> listar() {
        return tipoMaterialRepository.findAll();
    }
}
