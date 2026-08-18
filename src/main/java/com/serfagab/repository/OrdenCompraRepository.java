package com.serfagab.repository;

import com.serfagab.entities.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    List<OrdenCompra> findByUsuarioIdUsuario(Integer idUsuario);

    Page<OrdenCompra> findByUsuarioIdUsuario(Integer idUsuario, Pageable pageable);

    @Query("SELECT oc FROM OrdenCompra oc WHERE oc.estado = :estado")
    List<OrdenCompra> findByEstado(@Param("estado") String estado);
}
