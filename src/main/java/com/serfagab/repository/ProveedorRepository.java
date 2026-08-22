package com.serfagab.repository;

import com.serfagab.entities.Proveedor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.List;

public interface ProveedorRepository extends JpaRepository<Proveedor, Integer> {
    List<Proveedor> findByActivoTrue();

    List<Proveedor> findByRazonSocialContainingIgnoreCaseAndActivoTrue(String razonSocial);

    List<Proveedor> findByRucContainingAndActivoTrue(String ruc);

    List<Proveedor> findByEmailContainingIgnoreCaseAndActivoTrue(String email);

    Page<Proveedor> findByActivoTrue(Pageable pageable);

    Page<Proveedor> findByRazonSocialContainingIgnoreCaseAndActivoTrue(String razonSocial, Pageable pageable);

    Page<Proveedor> findByRucContainingAndActivoTrue(String ruc, Pageable pageable);

    Page<Proveedor> findByEmailContainingIgnoreCaseAndActivoTrue(String email, Pageable pageable);
}
