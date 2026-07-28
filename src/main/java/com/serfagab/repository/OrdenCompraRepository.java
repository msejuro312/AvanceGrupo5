package com.serfagab.repository;

import com.serfagab.entities.OrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface OrdenCompraRepository extends JpaRepository<OrdenCompra, Integer> {
    List<OrdenCompra> findByUsuarioIdUsuario(Integer idUsuario);
}
