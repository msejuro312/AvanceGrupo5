package com.serfagab.repository;

import com.serfagab.entities.TipoMaterial;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface TipoMaterialRepository extends JpaRepository<TipoMaterial, Integer> {
    List<TipoMaterial> findByActivoTrue();
}
