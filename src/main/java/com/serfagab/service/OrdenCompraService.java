package com.serfagab.service;

import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import com.serfagab.entities.DetalleOrdenCompra;
import com.serfagab.entities.Material;
import com.serfagab.entities.OrdenCompra;
import com.serfagab.entities.Proveedor;
import com.serfagab.entities.Usuario;
import com.serfagab.repository.DetalleOrdenCompraRepository;
import com.serfagab.repository.MaterialRepository;
import com.serfagab.repository.OrdenCompraRepository;
import com.serfagab.repository.ProveedorRepository;
import com.serfagab.repository.UsuarioRepository;
import java.time.LocalDate;
import java.util.List;

@Service
public class OrdenCompraService {
    private final OrdenCompraRepository ordenCompraRepository;
    private final DetalleOrdenCompraRepository detalleRepository;
    private final ProveedorRepository proveedorRepository;
    private final MaterialRepository materialRepository;
    private final UsuarioRepository usuarioRepository;

    public OrdenCompraService(OrdenCompraRepository ordenCompraRepository,
                              DetalleOrdenCompraRepository detalleRepository,
                              ProveedorRepository proveedorRepository,
                              MaterialRepository materialRepository,
                              UsuarioRepository usuarioRepository) {
        this.ordenCompraRepository = ordenCompraRepository;
        this.detalleRepository = detalleRepository;
        this.proveedorRepository = proveedorRepository;
        this.materialRepository = materialRepository;
        this.usuarioRepository = usuarioRepository;
    }

    @Transactional
    public OrdenCompra crearOrden(Integer idUsuario, Integer idProveedor, LocalDate fecha, String observaciones) {
        Usuario usuario = usuarioRepository.findById(idUsuario).orElseThrow();
        Proveedor proveedor = proveedorRepository.findById(idProveedor).orElseThrow();

        OrdenCompra orden = new OrdenCompra();
        orden.setUsuario(usuario);
        orden.setProveedor(proveedor);
        orden.setFecha(fecha);
        orden.setEstado("PENDIENTE");
        orden.setTotal(0.0);
        orden.setObservaciones(observaciones);
        return ordenCompraRepository.save(orden);
    }

    @Transactional
    public DetalleOrdenCompra agregarDetalle(Integer idOrden, Integer idMaterial, Double cantidad, Double precioUnitario) {
        OrdenCompra orden = ordenCompraRepository.findById(idOrden).orElseThrow();
        Material material = materialRepository.findById(idMaterial).orElseThrow();

        Double subtotal = cantidad * precioUnitario;

        DetalleOrdenCompra detalle = new DetalleOrdenCompra();
        detalle.setOrdenCompra(orden);
        detalle.setMaterial(material);
        detalle.setCantidad(cantidad);
        detalle.setPrecioUnitario(precioUnitario);
        detalle.setSubtotal(subtotal);
        detalleRepository.save(detalle);

        orden.setTotal(orden.getTotal() + subtotal);
        ordenCompraRepository.save(orden);

        return detalle;
    }

    public List<OrdenCompra> listarPorUsuario(Integer idUsuario) {
        return ordenCompraRepository.findByUsuarioIdUsuario(idUsuario);
    }

    public OrdenCompra obtenerConDetalles(Integer idOrden) {
        return ordenCompraRepository.findById(idOrden).orElseThrow();
    }

    public List<DetalleOrdenCompra> obtenerDetalles(Integer idOrden) {
        return detalleRepository.findByOrdenCompraIdOrdenCompra(idOrden);
    }
}
