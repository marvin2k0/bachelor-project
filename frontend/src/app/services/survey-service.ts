import {inject, Injectable} from '@angular/core';
import {Survey} from '../model/survey';
import {HttpClient} from '@angular/common/http';

@Injectable({
  providedIn: 'root',
})
export class SurveyService {
  readonly http: HttpClient = inject(HttpClient)
  baseUrl: string = "http://localhost:8080/api/v1/survey"

  create(survey: Survey) {
    return this.http.post(`${this.baseUrl}`, survey);
  }
}
