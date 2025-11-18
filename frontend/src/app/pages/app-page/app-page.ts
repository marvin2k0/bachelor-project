import {Component} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {Header} from '../../components/header/header';

@Component({
  selector: 'app-app-page',
  imports: [
    RouterOutlet,
    Header
  ],
  templateUrl: './app-page.html',
  styleUrl: './app-page.css',
})
export class AppPage {
}
