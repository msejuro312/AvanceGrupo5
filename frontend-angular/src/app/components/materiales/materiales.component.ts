import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Material } from '../../models/material';
import { TipoMaterial } from '../../models/tipo-material';
import { MaterialService } from '../../services/material.service';
import { TipoMaterialService } from '../../services/tipo-material.service';
import { SesionService } from '../../services/sesion.service';

@Component({
  selector: 'app-materiales',
  standalone: true,
  imports: [CommonModule, FormsModule],
  templateUrl: './materiales.component.html',
  styleUrl: './materiales.component.css'
})
export class MaterialesComponent implements OnInit {
  materiales: Material[] = [];
  tiposMaterial: TipoMaterial[] = [];
  cargando = true;
  textoBusqueda = '';
  editandoId: number | null = null;
  mostrarModal = false;
  errorMensaje = '';
  esAdmin = false;
  edicion: Material = {
    idMaterial: 0,
    tipoMaterial: null,
    nombre: '',
    unidadMedida: '',
    stockActual: 0,
    precioReferencial: 0,
    descripcion: '',
    activo: true,
    version: 0
  };

  constructor(
    private materialService: MaterialService,
    private tipoMaterialService: TipoMaterialService,
    private sesionService: SesionService
  ) {
    this.esAdmin = this.sesionService.esAdministrador();
  }

  ngOnInit(): void {
    this.listar();
    this.cargarTipos();
  }

  cargarTipos() {
    this.tipoMaterialService.listar().subscribe({
      next: (data) => (this.tiposMaterial = data),
      error: (err) => console.error('Error al listar tipos de material', err)
    });
  }

  listar() {
    this.cargando = true;
    this.materialService.listar().subscribe({
      next: (data) => {
        this.materiales = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar materiales', err);
        this.cargando = false;
      }
    });
  }

  buscar() {
    if (!this.textoBusqueda.trim()) {
      this.listar();
      return;
    }
    this.cargando = true;
    this.materialService.buscar(this.textoBusqueda).subscribe({
      next: (data) => {
        this.materiales = data;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al buscar materiales', err);
        this.cargando = false;
      }
    });
  }

  abrirNuevo() {
    this.editandoId = null;
    this.edicion = {
      idMaterial: 0,
      tipoMaterial: null,
      nombre: '',
      unidadMedida: '',
      stockActual: 0,
      precioReferencial: 0,
      descripcion: '',
      activo: true,
      version: 0
    };
    this.errorMensaje = '';
    this.mostrarModal = true;
  }

  abrirEditar(m: Material) {
    this.editandoId = m.idMaterial;
    this.edicion = {
      ...m,
      tipoMaterial: m.tipoMaterial ? { ...m.tipoMaterial } : null
    };
    this.errorMensaje = '';
    this.mostrarModal = true;
  }

  cancelarEdicion() {
    this.editandoId = null;
    this.mostrarModal = false;
  }

  guardar() {
    this.errorMensaje = '';
    const accion = this.editandoId !== null
      ? this.materialService.actualizar(this.editandoId, this.edicion)
      : this.materialService.crear(this.edicion);

    accion.subscribe({
      next: () => {
        this.mostrarToast('success', this.editandoId !== null
          ? 'Material actualizado correctamente'
          : 'Material creado correctamente');
        this.cancelarEdicion();
        this.listar();
      },
      error: (err) => {
        console.error('Error al guardar material', err);
        this.errorMensaje = 'No se pudo guardar el material.';
      }
    });
  }

  eliminar(m: Material) {
    if (confirm(`¿Inactivar el material "${m.nombre}"?`)) {
      this.materialService.eliminar(m.idMaterial).subscribe({
        next: () => {
          this.mostrarToast('success', 'Material eliminado correctamente');
          this.listar();
        },
        error: (err) => {
          console.error('Error al eliminar material', err);
          this.errorMensaje = 'No se pudo eliminar el material.';
        }
      });
    }
  }

  private mostrarToast(icono: string, titulo: string) {
    const Swal = (window as any).Swal;
    const Toast = Swal.mixin({
      toast: true,
      position: 'top-end',
      showConfirmButton: false,
      timer: 3000,
      timerProgressBar: true
    });
    Toast.fire({ icon: icono, title: titulo });
  }
}