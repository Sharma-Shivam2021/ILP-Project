import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';

export const authGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);

  if (!authService.currentUserValue) {
    router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
    return false;
  }

  // If user must change password, only allow /change-password
  if (authService.isFirstLogin && state.url !== '/change-password') {
    router.navigate(['/change-password']);
    return false;
  }

  return true;
};
