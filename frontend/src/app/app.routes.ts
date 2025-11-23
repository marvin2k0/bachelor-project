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
        path: '',
        redirectTo: 'home',
        pathMatch: 'full'
      },
      {
        path: 'home',
        loadComponent: () => import('./pages/surveys/surveys')
      },
      {
        path: 'surveys',
        loadComponent: () => import('./pages/surveys/surveys')
      },
      {
        path: 'survey/:id/edit',
        loadComponent: () => import('./pages/create-edit-survey/create-edit-survey')
      },
      {
        path: 'survey/:id',
        loadComponent: () => import('./pages/survey-page/survey-page')
      }
    ]
  },
  { path: '**', redirectTo: '' }
];
