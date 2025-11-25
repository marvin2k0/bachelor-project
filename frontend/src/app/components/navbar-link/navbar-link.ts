import {Component, inject, input} from '@angular/core';

@Component({
  selector: 'app-navbar-link',
  imports: [
  ],
  templateUrl: './navbar-link.html',
  styleUrl: './navbar-link.css',
})
export class NavbarLink {
  label = input.required<string>()
}
