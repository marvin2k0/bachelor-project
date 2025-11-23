import {Component, computed, inject, input} from '@angular/core';
import {Survey} from '../../model/survey';
import {toSignal} from '@angular/core/rxjs-interop';
import {map} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {SurveyService} from '../../services/survey-service';
import {TranslocoPipe} from '@jsverse/transloco';
import {DatePipe} from '@angular/common';

@Component({
  selector: 'app-survey-page',
  imports: [
    TranslocoPipe,
    DatePipe
  ],
  templateUrl: './survey-page.html',
  styleUrl: './survey-page.css',
})
export default class SurveyPage {
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly surveyService = inject(SurveyService)

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
