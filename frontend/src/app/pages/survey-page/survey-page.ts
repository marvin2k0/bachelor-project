import {Component, computed, inject } from '@angular/core';
import {Survey} from '../../model/survey';
import {toSignal} from '@angular/core/rxjs-interop';
import {map} from 'rxjs';
import {ActivatedRoute} from '@angular/router';
import {SurveyService} from '../../services/survey-service';
import {TranslocoPipe} from '@jsverse/transloco';
import {DatePipe, TitleCasePipe} from '@angular/common';
import {CdkDrag, CdkDragDrop, CdkDropList, moveItemInArray} from '@angular/cdk/drag-drop';
import {GroupDisplay} from '../../components/group-display/group-display';

@Component({
  selector: 'app-survey-page',
  imports: [
    TranslocoPipe,
    DatePipe,
    CdkDropList,
    GroupDisplay,
    CdkDrag,
    TitleCasePipe
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

  protected readonly groups = computed(() => this.survey()!.groups)

  drop(event: CdkDragDrop<string[]>) {
    moveItemInArray(this.groups()!, event.previousIndex, event.currentIndex)
    this.assignPriorities()
  }

  private assignPriorities() {
    for (let i = 0; i < this.groups()!.length; i++) {
      console.log(`Priorität ${i + 1} = ${this.groups()![i].name}`)
    }
  }
}
