import {Component, inject, OnInit} from '@angular/core';
import {RouterOutlet} from "@angular/router";
import {Header} from '../../components/header/header';
import {SurveyService} from '../../services/survey-service';
import {Modal} from '../../components/modal/modal';
import {ModalService} from '../../services/modal-service';

@Component({
  selector: 'app-app-page',
  imports: [
    RouterOutlet,
    Header,
    Modal
  ],
  templateUrl: './app-page.html',
  styleUrl: './app-page.css',
})
export class AppPage implements OnInit{
  private readonly service = inject(SurveyService)
  protected readonly modalService = inject(ModalService)

  ngOnInit() {
    this.service.loadSurveys();
  }
}
