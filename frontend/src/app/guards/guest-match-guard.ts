import { inject } from '@angular/core';
import { CanMatchFn } from '@angular/router';
import {AuthService} from '../services/auth-service';

export const guestMatchGuard: CanMatchFn = () => {
  const auth = inject(AuthService);
  return !auth.isLoggedIn();
};
