import { inject } from '@angular/core';
import { CanActivateFn, Router } from '@angular/router';
import { AuthService } from '../services/auth.service';
import { Role } from '../models/models';

export const roleGuard: CanActivateFn = (route, state) => {
  const authService = inject(AuthService);
  const router = inject(Router);
  const user = authService.currentUserValue;

  if (user) {
    const expectedRoles = route.data['roles'] as Array<Role>;
    if (expectedRoles && expectedRoles.includes(user.role)) {
      return true;
    }

    // Role not authorized, redirect to home/dashboard based on role
    router.navigate(['/']);
    return false;
  }

  router.navigate(['/login'], { queryParams: { returnUrl: state.url } });
  return false;
};
