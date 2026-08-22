package com.serfagab.repository;

import com.serfagab.entities.OrdenCompra;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import java.time.LocalDate;
import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    List<OrdenCompra> findByUsuarioIdUsuario(Integer idUsuario);

    Page<OrdenCompra> findByUsuarioIdUsuario(Integer idUsuario, Pageable pageable);

    @Query("SELECT oc FROM OrdenCompra oc WHERE oc.estado = :estado")
    List<OrdenCompra> findByEstado(@Param("estado") String estado);

    @Query("""
            SELECT oc FROM OrdenCompra oc
            WHERE oc.usuario.idUsuario = :idUsuario
              AND (:estado IS NULL OR oc.estado = :estado)
              AND (:idProveedor IS NULL OR oc.proveedor.idProveedor = :idProveedor)
              AND (:fechaDesde IS NULL OR oc.fecha >= :fechaDesde)
              AND (:fechaHasta IS NULL OR oc.fecha <= :fechaHasta)
            """)
    Page<OrdenCompra> buscarPaginadoConFiltros(@Param("idUsuario") Integer idUsuario,
                                               @Param("estado") String estado,
                                               @Param("idProveedor") Integer idProveedor,
                                               @Param("fechaDesde") LocalDate fechaDesde,
                                               @Param("fechaHasta") LocalDate fechaHasta,
                                               Pageable pageable);
}
