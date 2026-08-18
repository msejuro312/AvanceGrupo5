import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TipoMaterial } from '../../models/tipo-material';
import { TipoMaterialService } from '../../services/tipo-material.service';

@Component({
  selector: 'app-tipos-material',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './tipos-material.component.html',
  styleUrl: './tipos-material.component.css'
})
export class TiposMaterialComponent implements OnInit {
  tipos: TipoMaterial[] = [];
  cargando = true;
  errorMensaje = '';
  editandoId: number | null = null;
  mostrarModal = false;
  nuevo: TipoMaterial = {
    idTipoMaterial: 0,
    nombre: '',
    descripcion: '',
    activo: true
  };

  constructor(private tipoMaterialService: TipoMaterialService) {}

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    this.tipoMaterialService.listar().subscribe({
      next: (data) => {
        this.tipos = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar tipos de material', err);
        this.cargando = false;
      }
    });
  }

  guardar() {
    this.errorMensaje = '';
    if (!this.nuevo.nombre.trim()) {
      this.errorMensaje = 'El nombre es obligatorio.';
      return;
    }

    const accion = this.editandoId !== null
      ? this.tipoMaterialService.actualizar(this.editandoId, this.nuevo)
      : this.tipoMaterialService.crear(this.nuevo);

    accion.subscribe({
      next: () => {
        this.cancelarEdicion();
        this.listar();
      },
      error: (err) => {
        console.error('Error al guardar tipo de material', err);
        this.errorMensaje = 'No se pudo guardar el tipo de material.';
      }
    });
  }

  abrirNuevo() {
    this.editandoId = null;
    this.nuevo = {
      idTipoMaterial: 0,
      nombre: '',
      descripcion: '',
      activo: true
    };
    this.errorMensaje = '';
    this.mostrarModal = true;
  }

  abrirEditar(t: TipoMaterial) {
    this.editandoId = t.idTipoMaterial;
    this.nuevo = { ...t };
    this.errorMensaje = '';
    this.mostrarModal = true;
  }

  cancelarEdicion() {
    this.editandoId = null;
    this.mostrarModal = false;
  }

  eliminar(t: TipoMaterial) {
    if (confirm(`¿Inactivar el tipo de material "${t.nombre}"?`)) {
      this.tipoMaterialService.eliminar(t.idTipoMaterial).subscribe({
        next: () => this.listar(),
        error: (err) => {
          console.error('Error al eliminar tipo de material', err);
          this.errorMensaje = 'No se pudo eliminar el tipo de material.';
        }
      });
    }
  }
}