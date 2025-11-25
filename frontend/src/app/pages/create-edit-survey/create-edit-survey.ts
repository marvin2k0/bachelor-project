import {Component, computed, effect, inject} from '@angular/core';
import {toSignal} from '@angular/core/rxjs-interop';
import {ActivatedRoute, Router} from '@angular/router';
import {map} from 'rxjs';
import {Survey} from '../../model/survey';
import {SurveyService} from '../../services/survey-service';
import {InputField} from '../../components/input-field/input-field';
import {ButtonComponent} from '../../components/button/button.component';
import {TranslocoPipe} from '@jsverse/transloco';
import {AbstractControl, FormBuilder, ReactiveFormsModule, Validators} from '@angular/forms';
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
  private readonly router = inject(Router)

  protected readonly form = this.fb.group({
    name: ["", Validators.required],
    description: [""],
    startTime: [""],
    endTime: [""],
    groupCount: [1, [Validators.min(1)]]
  }, {validators: this.timeOrderValidator})

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
          description: s.description,
          endTime: this.formatDateForInput(s.endTime),
        });
      }
    });
  }

  protected onSubmitCreate() {
    if (this.form.invalid) return;
    this.surveyService.create({
      name: this.form.value.name!,
      description: this.form.value.description!,
      startTime: new Date(this.form.value.startTime!),
      endTime: new Date(this.form.value.endTime!),
      groups: [],
      groupCount: this.form.value.groupCount!
    })
      .subscribe({
        next: () => {
          this.surveyService.loadSurveys()
          this.router.navigate(["surveys"])
        }
      })
  }

  protected onSubmitUpdate() {
    if (this.form.invalid) return;
    this.surveyService.update({id: this.id()!, name: this.form.value.name!, description: this.form.value.description!, startTime: new Date(this.form.value.startTime!), endTime: new Date(this.form.value.endTime!), groups: []})
      .subscribe({next: () => {
          this.surveyService.loadSurveys()
          this.router.navigate(["survey/" + this.id()!]).then()
        }})
  }

  timeOrderValidator(control: AbstractControl) {
    const startTime = control.get('startTime')?.value;
    const endTime = control.get('endTime')?.value;

    if (!startTime || !endTime) {
      return null;
    }

    if (startTime > endTime) {
      return { timeOrder: true };
    }
    return null;
  }

  private formatDateForInput(value: Date | string | null | undefined): string | null {
    if (!value) {
      return null;
    }

    let date: Date;

    if (value instanceof Date) {
      date = value;
    } else {
      date = new Date(value);
    }

    const year = date.getFullYear();
    const month = String(date.getMonth() + 1).padStart(2, '0');
    const day = String(date.getDate()).padStart(2, '0');

    return `${year}-${month}-${day}`;
  }
}
