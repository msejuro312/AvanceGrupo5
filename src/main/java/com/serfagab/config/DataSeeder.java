package com.serfagab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import com.serfagab.entities.*;
import com.serfagab.repository.*;
import com.serfagab.service.OrdenCompraService;
import java.time.LocalDate;

@Component
public class DataSeeder implements CommandLineRunner {
    private final TipoRepository tipoRepository;
    private final UsuarioRepository usuarioRepository;
    private final ProveedorRepository proveedorRepository;
    private final TipoMaterialRepository tipoMaterialRepository;
    private final MaterialRepository materialRepository;
    private final OrdenCompraService ordenCompraService;
    private final OrdenCompraRepository ordenCompraRepository;

    public DataSeeder(TipoRepository tipoRepository, UsuarioRepository usuarioRepository,
                      ProveedorRepository proveedorRepository, TipoMaterialRepository tipoMaterialRepository,
                      MaterialRepository materialRepository,
                      OrdenCompraService ordenCompraService, OrdenCompraRepository ordenCompraRepository) {
        this.tipoRepository = tipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
        this.tipoMaterialRepository = tipoMaterialRepository;
        this.materialRepository = materialRepository;
        this.ordenCompraService = ordenCompraService;
        this.ordenCompraRepository = ordenCompraRepository;
    }

    @Override
    public void run(String... args) {
        if (tipoRepository.count() > 0) return;

        Tipo admin = new Tipo();
        admin.setIdTipo(1);
        admin.setDescripcion("administrador");
        tipoRepository.save(admin);

        Usuario user = new Usuario();
        user.setNombres("Valerie");
        user.setApellidos("Cavero");
        user.setLogin("admin");
        user.setClave("123");
        user.setEmail("vcavero@serfagab.com");
        user.setActivo(true);
        user.setTipo(admin);
        usuarioRepository.save(user);

        Proveedor p1 = new Proveedor();
        p1.setRazonSocial("Comercializadora Andina EIRL");
        p1.setRuc("20605478931");
        p1.setCelular("987456321");
        p1.setEmail("ventas@andina.com.pe");
        p1.setDescripcion("Proveedor de materiales de construccion");
        p1.setActivo(true);
        proveedorRepository.save(p1);

        Proveedor p2 = new Proveedor();
        p2.setRazonSocial("Distribuidora Industrial Peru E.I.R.L.");
        p2.setRuc("20587412366");
        p2.setCelular("956741258");
        p2.setEmail("contacto@dipindustrial.pe");
        p2.setDescripcion("Venta de equipos electronicos");
        p2.setActivo(true);
        proveedorRepository.save(p2);

        TipoMaterial tm1 = new TipoMaterial();
        tm1.setNombre("Plancha");
        tm1.setDescripcion("Son materiales metalicos para tablero");
        tm1.setActivo(true);
        tipoMaterialRepository.save(tm1);

        TipoMaterial tm2 = new TipoMaterial();
        tm2.setNombre("Pintura");
        tm2.setDescripcion("Gris ANSI 61");
        tm2.setActivo(true);
        tipoMaterialRepository.save(tm2);

        Material m1 = new Material();
        m1.setTipoMaterial(tm1);
        m1.setNombre("Perno 3x3");
        m1.setUnidadMedida("unidad");
        m1.setStockActual(10.0);
        m1.setPrecioReferencial(3.0);
        m1.setDescripcion("para tablero");
        m1.setActivo(true);
        materialRepository.save(m1);

        Material m2 = new Material();
        m2.setTipoMaterial(tm2);
        m2.setNombre("Perno 4x3");
        m2.setUnidadMedida("unidad");
        m2.setStockActual(12.0);
        m2.setPrecioReferencial(4.0);
        m2.setDescripcion("para tablero");
        m2.setActivo(true);
        materialRepository.save(m2);

        seedOrdenes();
    }

    private void seedOrdenes() {
        crearOrden(1, 1, 1, LocalDate.now().minusDays(5), 10.0, 3.0, "ENVIADO", "Compra de pernos para tablero");
        crearOrden(1, 2, 2, LocalDate.now().minusDays(4), 8.0, 4.0, "ENVIADO", "Compra de pernos 4x3");
        crearOrden(1, 1, 1, LocalDate.now().minusDays(3), 15.0, 3.0, "ENVIADO", "Reposicion de stock");
        crearOrden(1, 2, 2, LocalDate.now().minusDays(2), 5.0, 4.0, "PENDIENTE", "Orden en espera");
        crearOrden(1, 1, 2, LocalDate.now().minusDays(1), 20.0, 4.0, "PENDIENTE", "Pedido urgente");
        crearOrden(1, 2, 1, LocalDate.now(), 6.0, 3.0, "PENDIENTE", "Orden de hoy");
        crearOrdenConDosDetalles();
    }

    private void crearOrden(int idUsuario, int idProveedor, int idMaterial, LocalDate fecha,
                            double cantidad, double precio, String estado, String observaciones) {
        OrdenCompra orden = ordenCompraService.crearOrden(idUsuario, idProveedor, fecha, observaciones);
        ordenCompraService.agregarDetalle(orden.getIdOrdenCompra(), idMaterial, cantidad, precio);
        cambiarEstado(orden.getIdOrdenCompra(), estado);
    }

    private void crearOrdenConDosDetalles() {
        OrdenCompra orden = ordenCompraService.crearOrden(1, 1, LocalDate.now(), "Orden con dos materiales");
        ordenCompraService.agregarDetalle(orden.getIdOrdenCompra(), 1, 12.0, 3.0);
        ordenCompraService.agregarDetalle(orden.getIdOrdenCompra(), 2, 3.0, 4.0);
        cambiarEstado(orden.getIdOrdenCompra(), "PENDIENTE");
    }

    private void cambiarEstado(Integer idOrden, String estado) {
        ordenCompraRepository.findById(idOrden).ifPresent(orden -> {
            orden.setEstado(estado);
            ordenCompraRepository.save(orden);
        });
    }
}
