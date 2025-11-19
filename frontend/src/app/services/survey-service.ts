import {inject, Injectable, signal} from '@angular/core';
import {Survey} from '../model/survey';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SurveyService {
  private readonly http: HttpClient = inject(HttpClient)
  private readonly baseUrl: string = "http://localhost:8080/api/v1/survey/";

  surveys = signal<Survey[]>([])

  create(survey: Survey) {
    return this.http.post(`${this.baseUrl}`, survey);
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
