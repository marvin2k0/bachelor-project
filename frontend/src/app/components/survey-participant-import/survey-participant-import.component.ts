import {Component, ChangeDetectionStrategy, input, signal, inject, computed} from '@angular/core';
import { CommonModule } from '@angular/common';
import { SurveyService} from '../../services/survey-service';
import { ParticipantImportResult} from '../../model/participant-import-result';
import {Survey} from '../../model/survey';
import {ButtonComponent} from '../button/button.component';
import {TranslocoPipe, TranslocoService} from '@jsverse/transloco';

@Component({
  selector: 'app-survey-participant-import',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule, ButtonComponent, TranslocoPipe],
  templateUrl: './survey-participant-import.component.html',
  styleUrl: './survey-participant-import.component.css'
})
export class SurveyParticipantImportComponent {
  private readonly translocoService = inject(TranslocoService)

  survey = input.required<Survey>();
  surveyId = computed(() => this.survey().id);

  private readonly importService = inject(SurveyService);

  readonly selectedFile = signal<File | null>(null);
  readonly isUploading = signal(false);
  readonly result = signal<ParticipantImportResult | null>(null);
  readonly errorMessage = signal<string | null>(null);

  onFileSelected(event: Event): void {
    const target = event.target as HTMLInputElement | null;
    const file = target?.files?.[0] ?? null;

    this.selectedFile.set(file);
    this.errorMessage.set(null);
    this.result.set(null);
  }

  onSubmit(event: Event): void {
    event.preventDefault();
    const file = this.selectedFile();
    if (!file) {
      this.errorMessage.set(this.translocoService.translate("survey.import.error_select_file_first"));
      return;
    }

    this.isUploading.set(true);
    this.errorMessage.set(null);
    this.result.set(null);

    this.importService
      .uploadParticipants(this.surveyId()!, file)
      .subscribe({
        next: (res) => {
          this.result.set(res);
          this.isUploading.set(false);
        },
        error: () => {
          this.errorMessage.set(
            this.translocoService.translate("survey.import.error_message")
          );
          this.isUploading.set(false);
        }
      });
  }
}
