import {inject, Injectable, signal} from '@angular/core';
import {Survey} from '../model/survey';
import {HttpClient} from '@angular/common/http';
import {tap} from 'rxjs';

@Injectable({
  providedIn: 'root',
})
export class SurveyService {
  private readonly http: HttpClient = inject(HttpClient)
  private readonly baseUrl: string = "http://localhost:8080/api/v1/survey/";

  surveys = signal<Survey[]>([])

  create(survey: Survey) {
    return this.http.post<Survey>(`${this.baseUrl}`, survey);
  }

  delete(survey: Survey) {
    return this.http.delete<Survey>(`${this.baseUrl}${survey.id}`).pipe(
      tap(_ => {
        return this.surveys.update(surveys => surveys.filter(s => s.id !== survey.id))
      })
    );
  }

  loadSurveys() {
    this.http.get<Survey[]>(this.baseUrl).subscribe({
      next: data => {
        const converted = data.map(survey => ({
          ...survey,
          startTime: survey.startTime ? new Date(survey.startTime) : null,
          endTime: survey.endTime ? new Date(survey.endTime) : null
        }));

        this.surveys.set(converted)
      },
      error: err => console.error(err)
    })
  }
}
