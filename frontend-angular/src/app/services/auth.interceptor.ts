import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { SesionService } from './sesion.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const sesionService = inject(SesionService);
  const usuario = sesionService.obtenerUsuario();

  if (usuario) {
    const credentials = btoa(usuario.login + ':' + usuario.clave);
    const authReq = req.clone({
      setHeaders: {
        Authorization: 'Basic ' + credentials
      }
    });
    return next(authReq);
  }

  return next(req);
};
