import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Proveedor } from '../../models/proveedor';
import { ProveedorService } from '../../services/proveedor.service';

@Component({
  selector: 'app-proveedores',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './proveedores.component.html',
  styleUrl: './proveedores.component.css'
})
export class ProveedoresComponent implements OnInit {
  proveedores: Proveedor[] = [];
  cargando = true;
  errorMensaje = '';
  editandoId: number | null = null;
  nuevo: Proveedor = {
    idProveedor: null,
    razonSocial: '',
    ruc: '',
    celular: '',
    email: '',
    descripcion: '',
    activo: true
  };

  constructor(private proveedorService: ProveedorService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    this.proveedorService.listar().subscribe({
      next: (data) => {
        this.proveedores = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar proveedores', err);
        this.cargando = false;
      }
    });
  }

  guardar() {
    this.errorMensaje = '';
    const accion = this.editandoId !== null
      ? this.proveedorService.actualizar(this.editandoId, this.nuevo)
      : this.proveedorService.crear(this.nuevo);

    accion.subscribe({
      next: () => {
        this.cancelarEdicion();
        this.listar();
      },
      error: (err) => {
        console.error('Error al guardar proveedor', err);
        this.errorMensaje = 'No se pudo guardar el proveedor. Revisa que RUC, email y teléfono estén completos.';
      }
    });
  }

  abrirEditar(p: Proveedor) {
    this.editandoId = p.idProveedor ?? null;
    this.nuevo = { ...p };
    this.errorMensaje = '';
  }

  cancelarEdicion() {
    this.editandoId = null;
    this.nuevo = {
      idProveedor: null,
      razonSocial: '',
      ruc: '',
      celular: '',
      email: '',
      descripcion: '',
      activo: true
    };
    this.errorMensaje = '';
  }

  eliminar(p: Proveedor) {
    if (p.idProveedor == null) return;
    if (confirm(`¿Inactivar el proveedor "${p.razonSocial}"?`)) {
      this.proveedorService.eliminar(p.idProveedor).subscribe({
        next: () => this.listar(),
        error: (err) => {
          console.error('Error al eliminar proveedor', err);
          this.errorMensaje = 'No se pudo eliminar el proveedor.';
        }
      });
    }
  }

  esRucValido(): boolean {
    return /^\d{11}$/.test(this.nuevo.ruc);
  }

  esCelularValido(): boolean {
    return /^\d{9}$/.test(this.nuevo.celular);
  }

  esEmailValido(): boolean {
    return /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(this.nuevo.email);
  }

  formularioValido(): boolean {
    return this.esRucValido() && this.esCelularValido() && this.esEmailValido();
  }
}