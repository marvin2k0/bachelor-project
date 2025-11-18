import {Component, inject, input} from '@angular/core';
import {RouterLink} from '@angular/router';
import {NavbarService} from '../../services/navbar-service';

@Component({
  selector: 'app-navbar-link',
  imports: [
    RouterLink
  ],
  templateUrl: './navbar-link.html',
  styleUrl: './navbar-link.css',
})
export class NavbarLink {
  label = input.required<string>()
  link = input.required<string>()
  navbarService = inject(NavbarService)

  closeNavbar() {
    this.navbarService.open.set(false)
  }
}
