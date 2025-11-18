import {CanMatchFn} from '@angular/router';
import {inject} from '@angular/core';
import {AuthService} from '../services/auth-service';

export const authMatchGuard: CanMatchFn = (route, segments) => {
  const auth = inject(AuthService);
  return auth.isLoggedIn();
};
