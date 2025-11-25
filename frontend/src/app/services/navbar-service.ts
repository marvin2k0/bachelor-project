import {inject, Injectable, signal} from '@angular/core';
import {ActivatedRoute, Router} from '@angular/router';

@Injectable({
  providedIn: 'root',
})
export class NavbarService {
  private readonly router = inject(Router)
  private readonly route = inject(ActivatedRoute)

  open = signal(false)
  activeLink = signal<string>(this.getCurrentRoute())

  constructor() {
    this.router.events.subscribe(() => {
      this.activeLink.set(this.getCurrentRoute())
    })
  }

  redirect(path: string) {
    this.router.navigate([path]).then(() => {
      this.open.set(false)
      this.activeLink.set(path)
    })
  }

  private getCurrentRoute(): string {
    const path = this.router.url.split('?')[0].substring(1) || 'home'
    return path
  }
}
