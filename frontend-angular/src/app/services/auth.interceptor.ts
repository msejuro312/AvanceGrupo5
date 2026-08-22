import { HttpInterceptorFn } from '@angular/common/http';
import { inject } from '@angular/core';

import { SesionService } from './sesion.service';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const sesionService = inject(SesionService);
  const credenciales = sesionService.obtenerCredenciales();

  if (credenciales) {
    const credentials = btoa(credenciales.login + ':' + credenciales.clave);
    const authReq = req.clone({
      setHeaders: {
        Authorization: 'Basic ' + credentials
      }
    });
    return next(authReq);
  }

  return next(req);
};
