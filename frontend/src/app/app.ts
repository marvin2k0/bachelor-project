import {ButtonComponent} from './components/button/button.component';
import {Component, signal} from '@angular/core';
import {TranslocoPipe} from '@jsverse/transloco';

@Component({
  selector: 'app-root',
  imports: [ButtonComponent, TranslocoPipe],
  templateUrl: './app.html',
  styleUrl: './app.css'
})
export class App {
  protected readonly title = signal('frontend');
}
