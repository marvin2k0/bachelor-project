import {Component, computed, inject, OnInit} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {Header} from '../../components/header/header';
import {SurveyService} from '../../services/survey-service';

@Component({
  selector: 'app-app-page',
  imports: [
    RouterOutlet,
    Header
  ],
  templateUrl: './app-page.html',
  styleUrl: './app-page.css',
})
export class AppPage implements OnInit{
  private readonly service = inject(SurveyService)

  ngOnInit() {
    this.service.loadSurveys();
  }
}
