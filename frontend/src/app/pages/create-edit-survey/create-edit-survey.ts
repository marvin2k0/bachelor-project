import {Component, computed, effect, inject} from '@angular/core';
import {toSignal} from '@angular/core/rxjs-interop';
import {ActivatedRoute} from '@angular/router';
import {map} from 'rxjs';
import {Survey} from '../../model/survey';
import {SurveyService} from '../../services/survey-service';
import {InputField} from '../../components/input-field/input-field';
import {ButtonComponent} from '../../components/button/button.component';
import {TranslocoPipe} from '@jsverse/transloco';
import {FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
import {
  SurveyParticipantImportComponent
} from '../../components/survey-participant-import/survey-participant-import.component';

@Component({
  selector: 'app-create-edit-survey',
  imports: [
    InputField,
    ButtonComponent,
    TranslocoPipe,
    ReactiveFormsModule,
    SurveyParticipantImportComponent
  ],
  templateUrl: './create-edit-survey.html',
  styleUrl: './create-edit-survey.css',
})
export default class CreateEditSurvey {
  private readonly surveyService = inject(SurveyService)
  private readonly activatedRoute = inject(ActivatedRoute)
  private readonly fb = inject(FormBuilder)

  protected readonly form = this.fb.group({
    name: ["", Validators.required],
    description: [""]
  })

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

  constructor() {
    effect(() => {
      const s = this.survey();

      if (s) {
        this.form.patchValue({
          name: s.name,
          description: s.description
        });
      }
    });
  }
}
