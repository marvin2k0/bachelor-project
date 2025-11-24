import {Component, inject} from '@angular/core';
import {SurveyService} from '../../services/survey-service';
import {SurveyDisplayComponent} from '../../components/survey-display/survey-display.component';
import {TranslocoPipe, TranslocoService} from '@jsverse/transloco';
import {Survey} from '../../model/survey';
import {ModalService} from '../../services/modal-service';

@Component({
  selector: 'app-surveys',
  imports: [
    SurveyDisplayComponent,
    TranslocoPipe
  ],
  templateUrl: './surveys.html',
  styleUrl: './surveys.css',
})
export default class Surveys {
  private readonly translocoService = inject(TranslocoService)
  protected readonly service = inject(SurveyService)
  protected readonly modalService = inject(ModalService)

  onConfirmDeletion(survey: Survey) {
    this.modalService.show({
      title: this.translocoService.translate("survey.settings.delete_header", { survey: survey.name }),
      text: this.translocoService.translate("survey.settings.delete_body", { survey: survey.name }),
      design: "red",
      onAccept: () => {
        this.delete(survey)
        this.modalService.hide()
      }
    })
  }

  delete(survey: Survey) {
    this.service.delete(survey).subscribe({
      next: data => {
        console.log(`Successfully deleted survey '${survey.name}'`);
      }
    })
  }
}
