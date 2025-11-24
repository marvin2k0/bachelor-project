import {computed, inject, Injectable, signal} from '@angular/core';
import {Survey} from '../model/survey';
import {HttpClient} from '@angular/common/http';
import { environment } from '../../environments/environment.development';
import {tap} from 'rxjs';
import { ParticipantImportResult} from '../model/participant-import-result';
import { Observable} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SurveyService {
  private readonly http: HttpClient = inject(HttpClient)
  private readonly _surveys = signal<Survey[]>([]);
  private readonly baseUrl: string = `${environment.apiUrl}/api/v1/survey/`

  readonly surveys = computed(() => this._surveys());

  create(survey: Survey) {
    return this.http.post<Survey>(this.baseUrl, survey).pipe(
      tap(created => {
        const normalized: Survey = {
          ...created,
          startTime: created.startTime ? new Date(created.startTime) : null,
          endTime: created.endTime ? new Date(created.endTime) : null
        };

        this._surveys.update(list => [...list, normalized]);
      })
    );
  }

  delete(survey: Survey) {
    return this.http.delete(`${this.baseUrl}${survey.id}`).pipe(
      tap(() => {
        this._surveys.update(list => list.filter(s => s.id !== survey.id));
      })
    );
  }

  loadSurveys() {
    this.http.get<Survey[]>(this.baseUrl).subscribe({
      next: data => {
        const surveyData = data.map(survey => ({
          ...survey,
          startTime: survey.startTime ? new Date(survey.startTime) : null,
          endTime: survey.endTime ? new Date(survey.endTime) : null
        }));

        this._surveys.set(surveyData);
      },
      error: err => console.error(err)
    });
  }

  getSurveyById(id: number): Survey | null {
    return this._surveys().find(s => s.id === id) ?? null;
  }

  uploadParticipants(surveyId: number, file: File): Observable<ParticipantImportResult> {
    const formData = new FormData();
    formData.append('file', file);

    return this.http.post<ParticipantImportResult>(
      `${this.baseUrl}${surveyId}/participants/import`,
      formData
    );
  }
}
