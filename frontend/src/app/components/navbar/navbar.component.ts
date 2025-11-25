import {Component, inject, input} from '@angular/core';
import {NavbarLink} from '../navbar-link/navbar-link';
import {TranslocoPipe} from '@jsverse/transloco';
import {ButtonComponent} from '../button/button.component';
import {NavbarService} from '../../services/navbar-service';

@Component({
  selector: 'app-navbar',
  imports: [
    NavbarLink,
    TranslocoPipe,
    ButtonComponent,
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  protected readonly service = inject(NavbarService)

  open = input.required<boolean>()
}
