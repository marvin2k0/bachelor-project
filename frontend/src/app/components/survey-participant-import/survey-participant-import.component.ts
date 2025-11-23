import { Component, ChangeDetectionStrategy, input, signal, inject } from '@angular/core';
import { CommonModule } from '@angular/common';
import { SurveyService} from '../../services/survey-service';
import { ParticipantImportResult} from '../../model/participant-import-result';

@Component({
  selector: 'app-survey-participant-import',
  changeDetection: ChangeDetectionStrategy.OnPush,
  imports: [CommonModule],
  templateUrl: './survey-participant-import.component.html',
  styleUrl: './survey-participant-import.component.css'
})
export class SurveyParticipantImportComponent {
  //surveyId = input.required<number>();
  surveyId = 1;

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
      this.errorMessage.set('Bitte zuerst eine CSV-Datei auswählen.');
      return;
    }

    this.isUploading.set(true);
    this.errorMessage.set(null);
    this.result.set(null);

    this.importService
      .uploadParticipants(this.surveyId, file)
      .subscribe({
        next: (res) => {
          this.result.set(res);
          this.isUploading.set(false);
        },
        error: () => {
          this.errorMessage.set(
            'Der Import ist fehlgeschlagen. Bitte überprüfe die CSV-Datei und versuche es erneut.'
          );
          this.isUploading.set(false);
        }
      });
  }
}
