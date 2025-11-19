import {Component, input} from '@angular/core';
import {Survey} from '../../model/survey';

@Component({
  selector: 'app-survey-display',
  imports: [],
  templateUrl: './survey-display.component.html',
  styleUrl: './survey-display.component.css'
})
export class SurveyDisplayComponent {
  survey = input.required<Survey>()
}
