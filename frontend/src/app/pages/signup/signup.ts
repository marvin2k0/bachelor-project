import {Component, inject} from '@angular/core';
import {AppService} from '../../services/app-service';
import {Header} from '../../components/header/header';

@Component({
  selector: 'app-signup',
  imports: [
    Header
  ],
  templateUrl: './signup.html',
  styleUrl: './signup.css',
})
export default class Signup {
  appService = inject(AppService)
}
