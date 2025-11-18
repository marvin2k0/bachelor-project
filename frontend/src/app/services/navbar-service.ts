import {Injectable, signal} from '@angular/core';

@Injectable({
  providedIn: 'root',
})
export class NavbarService {
  open = signal(false)
}
