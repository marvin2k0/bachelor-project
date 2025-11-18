import {Component, input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NavbarLink} from '../navbar-link/navbar-link';
import {TranslocoPipe} from '@jsverse/transloco';
import {ButtonComponent} from '../button/button.component';

@Component({
  selector: 'app-navbar',
  imports: [
    NavbarLink,
    TranslocoPipe,
    ButtonComponent
  ],
  templateUrl: './navbar.component.html',
  styleUrl: './navbar.component.css'
})
export class NavbarComponent {
  open = input.required<boolean>()
}
