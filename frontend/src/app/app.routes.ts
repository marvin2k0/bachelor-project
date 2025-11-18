import {Routes} from '@angular/router';
import {AppPage} from './pages/app-page/app-page';
import {App} from './app';
import {guestMatchGuard} from './guards/guest-match-guard';
import {authMatchGuard} from './guards/auth-match-guard';

export const routes: Routes = [
  {
    path: '',
    component: App,
    canMatch: [guestMatchGuard],
    children: [
      { path: '', redirectTo: 'signup', pathMatch: 'full' },
      {
        path: 'signup',
        loadComponent: () => import('./pages/signup/signup'),
      }
    ]
  },
  {
    path: '',
    component: AppPage,
    canMatch: [authMatchGuard],
    children: [
      {
        path: 'home',
        loadComponent: () => import('./pages/home/home'),
      }
    ]
  },

  { path: '**', redirectTo: '' }
];
