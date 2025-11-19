import {Component, computed, inject} from '@angular/core';
import {SurveyService} from '../../services/survey-service';
import {SurveyDisplayComponent} from '../../components/survey-display/survey-display.component';

@Component({
  selector: 'app-surveys',
  imports: [
    SurveyDisplayComponent
  ],
  templateUrl: './surveys.html',
  styleUrl: './surveys.css',
})
export default class Surveys {
  service = inject(SurveyService)
  surveys = computed(() => this.service.surveys());
}
