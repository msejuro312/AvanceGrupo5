package com.serfagab.repository;

import com.serfagab.entities.DetalleOrdenCompra;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface DetalleOrdenCompraRepository extends JpaRepository<DetalleOrdenCompra, Integer> {
    List<DetalleOrdenCompra> findByOrdenCompraIdOrdenCompra(Integer idOrdenCompra);
}
