import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';

import { SesionService } from './sesion.service';

export const sesionGuard: CanActivateFn = () => {
  const sesionService = inject(SesionService);
  const router = inject(Router);

  if (sesionService.obtenerUsuario()) {
    return true;
  }
  return router.createUrlTree(['/login']);
};
