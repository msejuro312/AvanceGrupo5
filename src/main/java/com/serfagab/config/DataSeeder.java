package com.serfagab.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
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
    private final PasswordEncoder passwordEncoder;

    public DataSeeder(TipoRepository tipoRepository, UsuarioRepository usuarioRepository,
                      ProveedorRepository proveedorRepository, TipoMaterialRepository tipoMaterialRepository,
                      MaterialRepository materialRepository,
                      OrdenCompraService ordenCompraService, OrdenCompraRepository ordenCompraRepository,
                      PasswordEncoder passwordEncoder) {
        this.tipoRepository = tipoRepository;
        this.usuarioRepository = usuarioRepository;
        this.proveedorRepository = proveedorRepository;
        this.tipoMaterialRepository = tipoMaterialRepository;
        this.materialRepository = materialRepository;
        this.ordenCompraService = ordenCompraService;
        this.ordenCompraRepository = ordenCompraRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public void run(String... args) {
        if (tipoRepository.count() > 0) return;

        Tipo admin = new Tipo();
        admin.setIdTipo(1);
        admin.setDescripcion("administrador");
        tipoRepository.save(admin);

        Tipo usuario = new Tipo();
        usuario.setIdTipo(2);
        usuario.setDescripcion("usuario");
        tipoRepository.save(usuario);

        Usuario adminUser = new Usuario();
        adminUser.setNombres("Valerie");
        adminUser.setApellidos("Cavero");
        adminUser.setLogin("admin");
        adminUser.setClave(passwordEncoder.encode("admin123"));
        adminUser.setEmail("vcavero@serfagab.com");
        adminUser.setActivo(true);
        adminUser.setTipo(admin);
        usuarioRepository.save(adminUser);

        Usuario userConsulta = new Usuario();
        userConsulta.setNombres("Usuario");
        userConsulta.setApellidos("De Consulta");
        userConsulta.setLogin("user");
        userConsulta.setClave(passwordEncoder.encode("user123"));
        userConsulta.setEmail("consulta@serfagab.com");
        userConsulta.setActivo(true);
        userConsulta.setTipo(usuario);
        usuarioRepository.save(userConsulta);

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

        TipoMaterial tm3 = nuevoTipo("Tornilleria", "Tornillos, tuercas y arandelas");
        TipoMaterial tm4 = nuevoTipo("Cable electrico", "Conductores para tableros e instalaciones");
        TipoMaterial tm5 = nuevoTipo("Herramienta manual", "Herramientas de taller y montaje");
        TipoMaterial tm6 = nuevoTipo("Soldadura", "Electrodos e insumos de soldadura");
        TipoMaterial tm7 = nuevoTipo("Madera", "Tableros y listones de madera");
        TipoMaterial tm8 = nuevoTipo("Accesorio electrico", "Accesorios de instalacion");

        Proveedor p3 = nuevoProveedor("Metalurgica Lima Sur SAC", "20451238761", "987123456", "ventas@metalurgicalimasur.pe", "Estructuras metalicas y planchas");
        Proveedor p4 = nuevoProveedor("Ferreteria El Constructor EIRL", "20539847120", "956874123", "contacto@elconstructor.pe", "Ferreteria general y tornilleria");
        Proveedor p5 = nuevoProveedor("Suministros Electricos Norte SAC", "20674125893", "965412387", "info@senorte.com.pe", "Cables y accesorios electricos");
        Proveedor p6 = nuevoProveedor("Aceros del Centro S.A.C.", "20418529637", "974852963", "aceros@acerosdelcentro.pe", "Planchas y perfiles de acero");
        Proveedor p7 = nuevoProveedor("Pinturas del Pacifico EIRL", "20596874123", "983214567", "ventas@pinturaspacifico.pe", "Pinturas industriales y esmaltes");
        Proveedor p8 = nuevoProveedor("Importadora TorniPeru SAC", "20456987132", "951234678", "pedidos@torniperu.com.pe", "Importacion de tornilleria");
        Proveedor p9 = nuevoProveedor("Maderera La Selva E.I.R.L.", "20369741258", "978456321", "ventas@madereralaselva.pe", "Triplay y listones de madera");
        Proveedor p10 = nuevoProveedor("Soldaduras Peruanas SAC", "20578412369", "969321457", "contacto@soldperu.pe", "Electrodos y equipos de soldadura");
        Proveedor p11 = nuevoProveedor("Herramientas Profesionales Andinas SAC", "20632154879", "975321846", "ventas@handinas.pe", "Herramientas manuales y electricas");
        Proveedor p12 = nuevoProveedor("Cables y Conductores Peru EIRL", "20415963287", "982147369", "ventas@cyconductor.pe", "Conductores electricos THW y TW");
        Proveedor p13 = nuevoProveedor("Grupo Metalico Unificado SAC", "20587419632", "964852913", "compras@gmunificado.pe", "Suministros metalicos industriales");
        Proveedor p14 = nuevoProveedor("Distribuidora de Pinturas Union EIRL", "20365248179", "959687412", "ventas@dpunion.pe", "Distribucion de pinturas y thinner");
        Proveedor p15 = nuevoProveedor("Almacen Industrial Villa El Salvador SAC", "20473651289", "973159486", "atencion@aives.pe", "Almacen multiinsumos industriales");
        Proveedor p16 = nuevoProveedor("Comercial Ferromax E.I.R.L.", "20524139657", "987456912", "ventas@ferromax.pe", "Ferreteria industrial y tornilleria");

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

        materialRepository.save(nuevoMaterial(tm1, "Plancha GALV C24", "placa", 40.0, 85.0, "Plancha galvanizada calibre 24 para gabinete"));
        materialRepository.save(nuevoMaterial(tm1, "Plancha LAC C26", "placa", 35.0, 72.0, "Plancha laminada en frio calibre 26"));
        materialRepository.save(nuevoMaterial(tm1, "Plancha Aluminio 1mm", "placa", 15.0, 120.0, "Plancha de aluminio para tapas"));
        materialRepository.save(nuevoMaterial(tm2, "Pintura Anticorrosiva Rojo", "litro", 60.0, 28.0, "Base anticorrosiva para estructura metalica"));
        materialRepository.save(nuevoMaterial(tm2, "Esmalte Negro Brillante", "litro", 45.0, 32.0, "Acabado final para tableros"));
        materialRepository.save(nuevoMaterial(tm2, "Thinner Estandar", "litro", 80.0, 12.0, "Diluyente para esmaltes"));
        materialRepository.save(nuevoMaterial(tm3, "Tornillo 1/2 x 2 pulgadas", "unidad", 500.0, 1.8, "Tornillo hexagonal galvanizado"));
        materialRepository.save(nuevoMaterial(tm3, "Tornillo 1 x 4 pulgadas", "unidad", 300.0, 3.2, "Tornillo para estructura principal"));
        materialRepository.save(nuevoMaterial(tm3, "Tuerca 1/2", "unidad", 600.0, 0.9, "Tuerca hexagonal galvanizada"));
        materialRepository.save(nuevoMaterial(tm3, "Arandela plana 1/2", "unidad", 800.0, 0.5, "Arandela plana de acero"));
        materialRepository.save(nuevoMaterial(tm3, "Tarugo 8 x 1.5", "unidad", 400.0, 0.7, "Tarugo plástico para anclaje"));
        materialRepository.save(nuevoMaterial(tm4, "Cable THW 12", "metro", 900.0, 2.4, "Cable cobre 12 AWG 600V"));
        materialRepository.save(nuevoMaterial(tm4, "Cable THW 10", "metro", 650.0, 3.6, "Cable cobre 10 AWG 600V"));
        materialRepository.save(nuevoMaterial(tm4, "Cable TW 14", "metro", 750.0, 1.9, "Cable cobre 14 AWG tablero control"));
        materialRepository.save(nuevoMaterial(tm5, "Martillo de uña 16 oz", "unidad", 25.0, 38.0, "Martillo con mango de fibra"));
        materialRepository.save(nuevoMaterial(tm5, "Destornillador plano 6", "unidad", 50.0, 14.0, "Punta magnetica aislada"));
        materialRepository.save(nuevoMaterial(tm5, "Pinza universal 8 pulgadas", "unidad", 30.0, 26.0, "Pinza combo aislada 1000V"));
        materialRepository.save(nuevoMaterial(tm5, "Llave ajustable 10 pulgadas", "unidad", 20.0, 42.0, "Llave inglesa cromo vanadio"));
        materialRepository.save(nuevoMaterial(tm6, "Electrodo E6013 1/8", "kg", 120.0, 18.0, "Electrodo rutinario para acero"));
        materialRepository.save(nuevoMaterial(tm6, "Alambre soldadura MIG 0.9mm", "kg", 60.0, 35.0, "Rollo de alambre ER70S-6"));
        materialRepository.save(nuevoMaterial(tm7, "Triplay 4mm 122x244", "placa", 22.0, 65.0, "Tablero triplay uso general"));
        materialRepository.save(nuevoMaterial(tm7, "Liston 2x3 tornillo", "metro", 150.0, 9.5, "Liston de madera tornillo seco"));
        materialRepository.save(nuevoMaterial(tm8, "Interruptor simple 10A", "unidad", 200.0, 8.0, "Interruptor para panel de control"));
        materialRepository.save(nuevoMaterial(tm8, "Tomacorriente doble", "unidad", 150.0, 12.0, "Tomacorriente bipolar + tierra"));
        materialRepository.save(nuevoMaterial(tm8, "Bornera 12 puntos", "unidad", 90.0, 15.5, "Barra de conexion para tablero"));

        seedOrdenes();
        sembrarOrdenesDemo();
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

    private TipoMaterial nuevoTipo(String nombre, String descripcion) {
        TipoMaterial tm = new TipoMaterial();
        tm.setNombre(nombre);
        tm.setDescripcion(descripcion);
        tm.setActivo(true);
        return tipoMaterialRepository.save(tm);
    }

    private Proveedor nuevoProveedor(String razonSocial, String ruc, String celular, String email, String descripcion) {
        Proveedor p = new Proveedor();
        p.setRazonSocial(razonSocial);
        p.setRuc(ruc);
        p.setCelular(celular);
        p.setEmail(email);
        p.setDescripcion(descripcion);
        p.setActivo(true);
        return proveedorRepository.save(p);
    }

    private Material nuevoMaterial(TipoMaterial tipoMaterial, String nombre, String unidadMedida,
                                   double stockActual, double precioReferencial, String descripcion) {
        Material m = new Material();
        m.setTipoMaterial(tipoMaterial);
        m.setNombre(nombre);
        m.setUnidadMedida(unidadMedida);
        m.setStockActual(stockActual);
        m.setPrecioReferencial(precioReferencial);
        m.setDescripcion(descripcion);
        m.setActivo(true);
        return materialRepository.save(m);
    }

    private void sembrarOrdenesDemo() {
        String[] estados = {"PENDIENTE", "ENVIADO", "ANULADO"};
        for (int i = 0; i < 18; i++) {
            int idProveedor = (i % 16) + 1;
            int idMaterial = (i % 26) + 1;
            LocalDate fecha = LocalDate.now().minusDays(i * 2L + 1L);
            double cantidad = 4.0 + (i % 10);
            double precio = 3.0 + (i % 15);
            crearOrden(1, idProveedor, idMaterial, fecha, cantidad, precio,
                       estados[i % 3], "Orden de reposicion programada N-" + (i + 1));
        }
        crearOrdenConTresDetalles();
    }

    private void crearOrdenConTresDetalles() {
        OrdenCompra orden = ordenCompraService.crearOrden(1, 3, LocalDate.now().minusDays(7),
                "Orden con multiples materiales");
        ordenCompraService.agregarDetalle(orden.getIdOrdenCompra(), 7, 25.0, 1.8);
        ordenCompraService.agregarDetalle(orden.getIdOrdenCompra(), 12, 40.0, 2.4);
        ordenCompraService.agregarDetalle(orden.getIdOrdenCompra(), 24, 10.0, 8.0);
        cambiarEstado(orden.getIdOrdenCompra(), "PENDIENTE");
    }
}
