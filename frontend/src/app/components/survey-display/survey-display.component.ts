import {Component, inject, input, output} from '@angular/core';
import {Survey} from '../../model/survey';
import {TranslocoPipe} from '@jsverse/transloco';
import {DatePipe, NgOptimizedImage} from '@angular/common';
import {RouterLink} from '@angular/router';

@Component({
  selector: 'app-survey-display',
  imports: [
    TranslocoPipe,
    DatePipe,
    NgOptimizedImage,
    RouterLink
  ],
  templateUrl: './survey-display.component.html',
  styleUrl: './survey-display.component.css'
})
export class SurveyDisplayComponent {
  survey = input.required<Survey>()
  onDelete = output<Survey>()
}
