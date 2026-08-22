package com.serfagab.repository;

import com.serfagab.entities.TipoMaterial;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TipoMaterialRepository extends JpaRepository<TipoMaterial, Integer> {
    List<TipoMaterial> findByActivoTrue();

    List<TipoMaterial> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre);

    List<TipoMaterial> findByDescripcionContainingIgnoreCaseAndActivoTrue(String descripcion);

    Page<TipoMaterial> findByActivoTrue(Pageable pageable);

    Page<TipoMaterial> findByNombreContainingIgnoreCaseAndActivoTrue(String nombre, Pageable pageable);

    Page<TipoMaterial> findByDescripcionContainingIgnoreCaseAndActivoTrue(String descripcion, Pageable pageable);
}
