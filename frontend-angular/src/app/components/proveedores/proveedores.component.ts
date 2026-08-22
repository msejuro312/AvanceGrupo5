import { Component, OnInit } from '@angular/core';
import { CommonModule } from '@angular/common';
import { FormsModule } from '@angular/forms';

import { Proveedor } from '../../models/proveedor';
import { ProveedorService, ProveedorFiltros } from '../../services/proveedor.service';
import { SesionService } from '../../services/sesion.service';

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
  esAdmin = false;
  textoBusqueda = '';
  criterioBusqueda = 'razonSocial';
  page = 0;
  size = 5;
  totalPages = 0;
  totalElements = 0;
  nuevo: Proveedor = {
    idProveedor: null,
    razonSocial: '',
    ruc: '',
    celular: '',
    email: '',
    descripcion: '',
    activo: true
  };

  constructor(private proveedorService: ProveedorService, private sesionService: SesionService) {
    this.esAdmin = this.sesionService.esAdministrador();
  }

  ngOnInit(): void {
    this.listar();
  }

  listar() {
    this.cargando = true;
    const filtros: ProveedorFiltros = { criterio: this.criterioBusqueda };
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
    this.proveedorService.listarPaginado(this.page, this.size, filtros).subscribe({
      next: (data) => {
        this.proveedores = data.content ?? [];
        this.totalPages = data.totalPages ?? 0;
        this.totalElements = data.totalElements ?? 0;
        this.cargando = false;
      },
      error: (err) => {
        console.error('Error al listar proveedores', err);
        this.proveedores = [];
        this.totalPages = 0;
        this.totalElements = 0;
        this.cargando = false;
      }
    });
  }

  private buscarPorId(id: number) {
    this.proveedorService.obtener(id).subscribe({
      next: (p) => {
        this.proveedores = [p];
        this.totalPages = 1;
        this.totalElements = 1;
        this.cargando = false;
      },
      error: () => {
        this.proveedores = [];
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
    this.criterioBusqueda = 'razonSocial';
    this.textoBusqueda = '';
    this.page = 0;
    this.listar();
  }

  guardar() {
    this.errorMensaje = '';
    const accion = this.editandoId !== null
      ? this.proveedorService.actualizar(this.editandoId, this.nuevo)
      : this.proveedorService.crear(this.nuevo);

    accion.subscribe({
      next: () => {
        this.mostrarToast('success', this.editandoId !== null
          ? 'Proveedor actualizado correctamente'
          : 'Proveedor creado correctamente');
        this.cancelarEdicion();
        this.page = 0;
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
        next: () => {
          this.mostrarToast('success', 'Proveedor eliminado correctamente');
          if (this.proveedores.length === 1 && this.page > 0) {
            this.page--;
          }
          this.listar();
        },
        error: (err) => {
          console.error('Error al eliminar proveedor', err);
          this.errorMensaje = 'No se pudo eliminar el proveedor.';
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