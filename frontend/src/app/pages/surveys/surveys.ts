import {Component, computed, inject, signal} from '@angular/core';
import {SurveyService} from '../../services/survey-service';
import {SurveyDisplayComponent} from '../../components/survey-display/survey-display.component';
import {TranslocoPipe} from '@jsverse/transloco';

@Component({
  selector: 'app-surveys',
  imports: [
    SurveyDisplayComponent,
    TranslocoPipe
  ],
  templateUrl: './surveys.html',
  styleUrl: './surveys.css',
})
export default class Surveys {
  service = inject(SurveyService)
  surveys = computed(() => this.service.surveys());

  ngOnInit() {
    this.service.loadSurveys();
  }
}
