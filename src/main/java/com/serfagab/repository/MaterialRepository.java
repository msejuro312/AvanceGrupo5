package com.serfagab.repository;

import com.serfagab.entities.Material;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface MaterialRepository extends JpaRepository<Material, Integer> {

    List<Material> findByNombreContainingIgnoreCase(String nombre);

    List<Material> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<Material> findByActivoTrue();

    @Query("SELECT m FROM Material m WHERE LOWER(m.nombre) LIKE LOWER(CONCAT('%', :texto, '%')) AND m.activo = true")
    List<Material> buscarPorNombre(@Param("texto") String texto);

    List<Material> findByTipoMaterial_IdTipoMaterialAndActivoTrue(Integer idTipo);

    List<Material> findByDescripcionContainingIgnoreCaseAndActivoTrue(String descripcion);

    Page<Material> findByActivoTrue(Pageable pageable);

    Page<Material> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);

    Page<Material> findByDescripcionContainingIgnoreCaseAndActivoTrue(String descripcion, Pageable pageable);

    Page<Material> findByTipoMaterial_IdTipoMaterialAndActivoTrue(Integer idTipo, Pageable pageable);

}
