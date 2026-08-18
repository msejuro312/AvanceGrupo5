import { HttpInterceptorFn } from '@angular/common/http';

export const authInterceptor: HttpInterceptorFn = (req, next) => {
  const credentials = btoa('user:user123');
  const authReq = req.clone({
    setHeaders: {
      Authorization: 'Basic ' + credentials
    }
  });
  return next(authReq);
};