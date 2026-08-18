import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { OrdenCompra } from '../../models/orden-compra';
import { Proveedor } from '../../models/proveedor';
import { Material } from '../../models/material';
import { OrdenService } from '../../services/orden.service';
import { ProveedorService } from '../../services/proveedor.service';
import { MaterialService } from '../../services/material.service';

interface FilaDetalle {
  materialId: number | null;
  cantidad: number;
  precioUnitario: number;
}

@Component({
  selector: 'app-ordenes',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './ordenes.component.html',
  styleUrl: './ordenes.component.css'
})
export class OrdenesComponent implements OnInit {
  ordenes: OrdenCompra[] = [];
  proveedores: Proveedor[] = [];
  materiales: Material[] = [];

  cargando = true;
  page = 0;
  size = 5;
  totalPages = 0;
  totalElements = 0;

  idProveedor: number | null = null;
  fecha: string = new Date().toISOString().substring(0, 10);
  observaciones = '';
  detalles: FilaDetalle[] = [];
  mensajeExito = '';
  mensajeError = '';
  guardando = false;

  detalleOrden: OrdenCompra | null = null;
  mostrarDetalle = false;
  estados = ['PENDIENTE', 'ENVIADO', 'ANULADO'];

  constructor(
    private ordenService: OrdenService,
    private proveedorService: ProveedorService,
    private materialService: MaterialService
  ) {}

  ngOnInit(): void {
    this.cargar();
    this.cargarProveedores();
    this.cargarMateriales();
  }

  cargar() {
    this.cargando = true;
    this.ordenService.historialPaginado(1, this.page, this.size).subscribe({
      next: (data) => {
        this.ordenes = data.content;
        this.totalPages = data.totalPages;
        this.totalElements = data.totalElements;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar órdenes', err);
        this.cargando = false;
      }
    });
  }

  cargarProveedores() {
    this.proveedorService.listar().subscribe({
      next: (data) => (this.proveedores = data),
      error: (err) => console.error('Error al listar proveedores', err)
    });
  }

  cargarMateriales() {
    this.materialService.listar().subscribe({
      next: (data) => (this.materiales = data),
      error: (err) => console.error('Error al listar materiales', err)
    });
  }

  anterior() {
    if (this.page > 0) {
      this.page--;
      this.cargar();
    }
  }

  siguiente() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.cargar();
    }
  }

  agregarFila() {
    this.detalles.push({ materialId: null, cantidad: 1, precioUnitario: 0 });
  }

  quitarFila(index: number) {
    this.detalles.splice(index, 1);
  }

  totalEstimado(): number {
    return this.detalles.reduce(
      (acc, d) => acc + (d.cantidad || 0) * (d.precioUnitario || 0),
      0
    );
  }

  guardar() {
    this.mensajeExito = '';
    this.mensajeError = '';

    if (!this.idProveedor || !this.fecha || this.detalles.length === 0) {
      this.mensajeError = 'Selecciona un proveedor, una fecha y al menos un detalle.';
      return;
    }

    this.guardando = true;
    const orden: any = {
      proveedor: { idProveedor: this.idProveedor },
      fecha: this.fecha,
      observaciones: this.observaciones
    };

    this.ordenService.crearOrden(1, orden).subscribe({
      next: (creada) => {
        this.agregarDetallesSecuenciales(creada.idOrdenCompra, 0);
      },
      error: (err) => {
        console.error('Error al crear la orden', err);
        this.guardando = false;
        this.mensajeError = 'No se pudo crear la orden. Verifica los datos.';
      }
    });
  }

  private agregarDetallesSecuenciales(idOrden: number, index: number) {
    if (index >= this.detalles.length) {
      this.guardando = false;
      this.mensajeExito = 'Orden de compra creada correctamente.';
      this.limpiarFormulario();
      this.page = 0;
      this.cargar();
      return;
    }

    const d = this.detalles[index];
    if (!d.materialId) {
      this.agregarDetallesSecuenciales(idOrden, index + 1);
      return;
    }

    const detalle = {
      material: { idMaterial: d.materialId },
      cantidad: d.cantidad,
      precioUnitario: d.precioUnitario
    };

    this.ordenService.agregarDetalle(idOrden, detalle).subscribe({
      next: () => this.agregarDetallesSecuenciales(idOrden, index + 1),
      error: (err) => {
        console.error('Error al agregar detalle', err);
        this.guardando = false;
        this.mensajeError = 'La orden se creó pero falló un detalle.';
        this.cargar();
      }
    });
  }

  private limpiarFormulario() {
    this.idProveedor = null;
    this.fecha = new Date().toISOString().substring(0, 10);
    this.observaciones = '';
    this.detalles = [];
  }

  verDetalles(o: OrdenCompra) {
    this.ordenService.detalle(o.idOrdenCompra).subscribe({
      next: (data) => {
        this.detalleOrden = data;
        this.mostrarDetalle = true;
      },
      error: (err) => {
        console.error('Error al obtener detalles de la orden', err);
        this.mensajeError = 'No se pudieron cargar los detalles de la orden.';
      }
    });
  }

  cerrarDetalle() {
    this.mostrarDetalle = false;
    this.detalleOrden = null;
  }

  cambiarEstado(o: OrdenCompra, event: any) {
    const estado = event.target.value;
    this.ordenService.cambiarEstado(o.idOrdenCompra, estado).subscribe({
      next: () => {
        this.mensajeError = '';
        this.mensajeExito = `Estado de la orden #${o.idOrdenCompra} actualizado a ${estado}.`;
        this.cargar();
      },
      error: (err) => {
        console.error('Error al cambiar estado', err);
        this.mensajeExito = '';
        this.mensajeError = 'No se pudo cambiar el estado de la orden.';
      }
    });
  }
}