package com.serfagab.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Data;

@Data
@Entity
@Table(name = "tbl_tipo")
public class Tipo {
    @Id
    @Column(name = "id_tipo")
    private Integer idTipo;

    @Column(name = "descripcion")
    private String descripcion;
}
