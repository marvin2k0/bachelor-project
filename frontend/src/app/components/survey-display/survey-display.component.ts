import {Component, input} from '@angular/core';
import {Survey} from '../../model/survey';
import {TranslocoPipe} from '@jsverse/transloco';
import {DatePipe, NgOptimizedImage} from '@angular/common';

@Component({
  selector: 'app-survey-display',
  imports: [
    TranslocoPipe,
    DatePipe,
    NgOptimizedImage
  ],
  templateUrl: './survey-display.component.html',
  styleUrl: './survey-display.component.css'
})
export class SurveyDisplayComponent {
  survey = input.required<Survey>()
}
