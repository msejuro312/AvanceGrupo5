import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { RouterLink } from '@angular/router';

import { OrdenCompra } from '../../models/orden-compra';
import { MaterialService } from '../../services/material.service';
import { ProveedorService } from '../../services/proveedor.service';
import { TipoMaterialService } from '../../services/tipo-material.service';
import { OrdenService } from '../../services/orden.service';
import { SesionService } from '../../services/sesion.service';

@Component({
  selector: 'app-dashboard',
  standalone: true,
  imports: [CommonModule, RouterLink],
  templateUrl: './dashboard.component.html',
  styleUrl: './dashboard.component.css'
})
export class DashboardComponent implements OnInit {
  totalMateriales = 0;
  totalProveedores = 0;
  totalTiposMaterial = 0;
  totalOrdenes = 0;
  ordenesPendientes = 0;
  ultimasOrdenes: OrdenCompra[] = [];
  nombreUsuario = '';

  constructor(
    private materialService: MaterialService,
    private proveedorService: ProveedorService,
    private tipoMaterialService: TipoMaterialService,
    private ordenService: OrdenService,
    private sesionService: SesionService
  ) {}

  ngOnInit(): void {
    const usuario = this.sesionService.obtenerUsuario();
    this.nombreUsuario = usuario ? `${usuario.nombres} ${usuario.apellidos}` : '';

    this.materialService.listar().subscribe({
      next: (data) => (this.totalMateriales = data.length)
    });

    this.proveedorService.listar().subscribe({
      next: (data) => (this.totalProveedores = data.length)
    });

    this.tipoMaterialService.listar().subscribe({
      next: (data) => (this.totalTiposMaterial = data.length)
    });

    this.ordenService.historialPaginado(1, 0, 1000).subscribe({
      next: (data) => {
        this.totalOrdenes = data.totalElements;
        this.ordenesPendientes = data.content.filter(
          (o: OrdenCompra) => o.estado === 'PENDIENTE'
        ).length;
        this.ultimasOrdenes = [...data.content]
          .sort((a: OrdenCompra, b: OrdenCompra) => b.idOrdenCompra - a.idOrdenCompra)
          .slice(0, 5);
      }
    });
  }
}
