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

  constructor() {
    this.loadSurveys()
  }

  create(survey: Survey) {
    return this.http.post(`${this.baseUrl}`, survey);
  }

  private loadSurveys() {
    this.http.get<Survey[]>(this.baseUrl).subscribe({
      next: data => this.surveys.set(data),
      error: err => console.error(err)
    })
  }
}
