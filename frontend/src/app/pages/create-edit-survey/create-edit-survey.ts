import {Component, computed, inject} from '@angular/core';
import {toSignal} from '@angular/core/rxjs-interop';
import {ActivatedRoute} from '@angular/router';
import {map} from 'rxjs';
import {Survey} from '../../model/survey';
import {SurveyService} from '../../services/survey-service';

@Component({
  selector: 'app-create-edit-survey',
  imports: [],
  templateUrl: './create-edit-survey.html',
  styleUrl: './create-edit-survey.css',
})
export default class CreateEditSurvey {
  private readonly surveyService = inject(SurveyService)
  private readonly activatedRoute = inject(ActivatedRoute)

  protected readonly id = toSignal(
    this.activatedRoute.paramMap.pipe(
      map(p => {
        const idString = p.get("id");
        return idString !== null ? +idString : null;
      })),
    {initialValue: null}
  )

  protected readonly survey = computed<Survey | null>(() => {
    const surveyId = this.id();

    if (!surveyId) return null;

    return this.surveyService.getSurveyById(surveyId);
  })
}
