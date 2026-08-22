import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { TipoMaterial } from '../../models/tipo-material';
import { TipoMaterialService, TipoMaterialFiltros } from '../../services/tipo-material.service';
import { SesionService } from '../../services/sesion.service';

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
  esAdmin = false;
  textoBusqueda = '';
  criterioBusqueda = 'nombre';
  page = 0;
  size = 5;
  totalPages = 0;
  totalElements = 0;
  nuevo: TipoMaterial = {
    idTipoMaterial: 0,
    nombre: '',
    descripcion: '',
    activo: true
  };

  constructor(private tipoMaterialService: TipoMaterialService, private sesionService: SesionService) {
    this.esAdmin = this.sesionService.esAdministrador();
  }

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    const filtros: TipoMaterialFiltros = { criterio: this.criterioBusqueda };
    if (this.textoBusqueda.trim()) {
      filtros.texto = this.textoBusqueda.trim();
    }
    if (this.criterioBusqueda === 'id' && filtros.texto) {
      const id = Number(filtros.texto);
      if (!Number.isNaN(id)) {
        this.buscarPorId(id);
        return;
      }
    }
    this.tipoMaterialService.listarPaginado(this.page, this.size, filtros).subscribe({
      next: (data) => {
        this.tipos = data.content ?? [];
        this.totalPages = data.totalPages ?? 0;
        this.totalElements = data.totalElements ?? 0;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar tipos de material', err);
        this.tipos = [];
        this.totalPages = 0;
        this.totalElements = 0;
        this.cargando = false;
      }
    });
  }

  private buscarPorId(id: number) {
    this.tipoMaterialService.obtener(id).subscribe({
      next: (t) => {
        this.tipos = [t];
        this.totalPages = 1;
        this.totalElements = 1;
        this.cargando = false;
      },
      error: () => {
        this.tipos = [];
        this.totalPages = 0;
        this.totalElements = 0;
        this.cargando = false;
      }
    });
  }

  buscar() {
    this.page = 0;
    this.listar();
  }

  anterior() {
    if (this.page > 0) {
      this.page--;
      this.listar();
    }
  }

  siguiente() {
    if (this.page < this.totalPages - 1) {
      this.page++;
      this.listar();
    }
  }

  alCambiarCriterio() {
    this.textoBusqueda = '';
    this.page = 0;
    this.listar();
  }

  limpiarFiltros() {
    this.criterioBusqueda = 'nombre';
    this.textoBusqueda = '';
    this.page = 0;
    this.listar();
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
        this.mostrarToast('success', this.editandoId !== null
          ? 'Tipo de material actualizado correctamente'
          : 'Tipo de material creado correctamente');
        this.cancelarEdicion();
        this.page = 0;
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
        next: () => {
          this.mostrarToast('success', 'Tipo de material eliminado correctamente');
          if (this.tipos.length === 1 && this.page > 0) {
            this.page--;
          }
          this.listar();
        },
        error: (err) => {
          console.error('Error al eliminar tipo de material', err);
          this.errorMensaje = 'No se pudo eliminar el tipo de material.';
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