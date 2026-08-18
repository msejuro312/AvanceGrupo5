package com.serfagab.entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_material")
public class Material {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id_material")
    private Integer idMaterial;

    @ManyToOne
    @JoinColumn(name = "id_tipo_material")
    private TipoMaterial tipoMaterial;

    @Column(name = "nombre")
    private String nombre;

    @Column(name = "unidad_medida")
    private String unidadMedida;

    @Column(name = "stock_actual")
    private Double stockActual = 0.0;

    @Column(name = "precio_referencial")
    private Double precioReferencial = 0.0;

    @Column(name = "descripcion")
    private String descripcion;

    @Column(name = "activo")
    private Boolean activo = true;

    @Version
    private Integer version; // Control de concurrencia optimista
}
