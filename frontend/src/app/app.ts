import {Component, inject, signal} from '@angular/core';
import {TranslocoPipe, TranslocoService} from '@jsverse/transloco';

@Component({
  selector: 'app-root',
  imports: [TranslocoPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
