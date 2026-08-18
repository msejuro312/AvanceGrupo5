package com.serfagab.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import com.serfagab.entities.Material;
import com.serfagab.repository.MaterialRepository;
import com.serfagab.service.MaterialService;
import java.util.List;

@RestController
@RequestMapping("/api/materiales")
public class MaterialController {
    private final MaterialService materialService;
    private final MaterialRepository materialRepository;

    public MaterialController(MaterialService materialService, MaterialRepository materialRepository) {
        this.materialService = materialService;
        this.materialRepository = materialRepository;
    }

    @PostMapping("/lote")
    public ResponseEntity<String> registrarLote(@RequestBody List<Material> materiales) {
        materialService.registrarLote(materiales);
        return ResponseEntity.ok("Materiales registrados satisfactoriamente.");
    }

    @PostMapping
    public ResponseEntity<Material> crear(@RequestBody Material material) {
        material.setIdMaterial(null);
        material.setVersion(null);
        return ResponseEntity.ok(materialRepository.save(material));
    }

    @GetMapping
    public List<Material> listar() {
        return materialService.listarTodos();
    }

    @GetMapping("/{id}")
    public ResponseEntity<Material> obtenerPorId(@PathVariable Integer id) {
        return materialRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping("/buscar/{nombre}")
    public List<Material> buscarPorNombre(@PathVariable String nombre) {
        return materialService.buscar(nombre);
    }

    @GetMapping("/buscarPorNombre")
    public ResponseEntity<List<Material>> buscarPorNombreParam(@RequestParam String texto) {
        List<Material> resultados = materialRepository.buscarPorNombre(texto);
        if (resultados.isEmpty()) {
            return ResponseEntity.notFound().build();
        }
        return ResponseEntity.ok(resultados);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Material> eliminar(@PathVariable Integer id) {
        return materialRepository.findById(id)
                .map(m -> {
                    m.setActivo(false);
                    return ResponseEntity.ok(materialRepository.save(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<Material> actualizar(@PathVariable Integer id, @RequestBody Material material) {
        return materialRepository.findById(id)
                .map(m -> {
                    m.setNombre(material.getNombre());
                    m.setUnidadMedida(material.getUnidadMedida());
                    m.setStockActual(material.getStockActual());
                    m.setPrecioReferencial(material.getPrecioReferencial());
                    m.setDescripcion(material.getDescripcion());
                    m.setTipoMaterial(material.getTipoMaterial());
                    return ResponseEntity.ok(materialRepository.save(m));
                })
                .orElse(ResponseEntity.notFound().build());
    }
}
