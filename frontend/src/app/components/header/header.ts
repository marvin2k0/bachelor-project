import {Component, computed, inject, input} from '@angular/core';
import {NavbarComponent} from "../navbar/navbar.component";
import {NgClass} from '@angular/common';
import {NavbarService} from '../../services/navbar-service';

@Component({
  selector: 'app-header',
  imports: [
    NavbarComponent,
    NgClass
  ],
  templateUrl: './header.html',
  styleUrl: './header.css',
})
export class Header {
  full = input<boolean>(true);
  navbarService = inject(NavbarService)
  open = computed(() => this.navbarService.open())

  toggleNavbar() {
    this.navbarService.open.update(v => !v);
  }
}
