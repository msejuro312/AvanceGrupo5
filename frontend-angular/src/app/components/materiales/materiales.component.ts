import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Material } from '../../models/material';
import { TipoMaterial } from '../../models/tipo-material';
import { MaterialService, MaterialFiltros } from '../../services/material.service';
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
  page = 0;
  size = 5;
  totalPages = 0;
  totalElements = 0;
  textoBusqueda = '';
  criterioBusqueda = 'nombre';
  idTipoSeleccionado: number | null = null;
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
    const filtros: MaterialFiltros = { criterio: this.criterioBusqueda };
    if ((this.criterioBusqueda === 'nombre' || this.criterioBusqueda === 'descripcion') && this.textoBusqueda.trim()) {
      filtros.texto = this.textoBusqueda.trim();
    }
    if (this.criterioBusqueda === 'tipo' && this.idTipoSeleccionado != null) {
      filtros.idTipo = this.idTipoSeleccionado;
    }
    if (this.criterioBusqueda === 'id' && this.textoBusqueda.trim()) {
      const id = Number(this.textoBusqueda);
      if (!Number.isNaN(id)) {
        this.buscarPorId(id);
        return;
      }
    }
    this.materialService.listarPaginado(this.page, this.size, filtros).subscribe({
      next: (data) => {
        this.materiales = data.content ?? [];
        this.totalPages = data.totalPages ?? 0;
        this.totalElements = data.totalElements ?? 0;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar materiales', err);
        this.materiales = [];
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

  private buscarPorId(id: number) {
    this.materialService.obtener(id).subscribe({
      next: (m) => {
        this.materiales = [m];
        this.totalPages = 1;
        this.totalElements = 1;
        this.cargando = false;
      },
      error: () => {
        this.materiales = [];
        this.totalPages = 0;
        this.totalElements = 0;
        this.cargando = false;
      }
    });
  }

  alCambiarCriterio() {
    this.textoBusqueda = '';
    this.idTipoSeleccionado = null;
    this.page = 0;
    this.listar();
  }

  limpiarFiltros() {
    this.criterioBusqueda = 'nombre';
    this.textoBusqueda = '';
    this.idTipoSeleccionado = null;
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
        this.page = 0;
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
          if (this.materiales.length === 1 && this.page > 0) {
            this.page--;
          }
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