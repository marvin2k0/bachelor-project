import {Component, inject} from '@angular/core';
import {SurveyService} from '../../services/survey-service';
import {SurveyDisplayComponent} from '../../components/survey-display/survey-display.component';
import {TranslocoPipe} from '@jsverse/transloco';
import {Survey} from '../../model/survey';

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
  protected readonly service = inject(SurveyService)

  delete(survey: Survey) {
    this.service.delete(survey).subscribe({
      next: data => {
        console.log(`Successfully deleted survey '${survey.name}'`);
      }
    })
  }
}
