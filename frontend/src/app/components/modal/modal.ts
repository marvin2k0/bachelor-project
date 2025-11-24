import {Component, inject, input, output} from '@angular/core';
import {ButtonComponent} from '../button/button.component';
import {ModalService} from '../../services/modal-service';
import {TranslocoPipe} from '@jsverse/transloco';

@Component({
  selector: 'app-modal',
  imports: [
    ButtonComponent,
    TranslocoPipe
  ],
  templateUrl: './modal.html',
  styleUrl: './modal.css',
})
export class Modal {
  protected readonly service = inject(ModalService)

  onDeny = output()
  onAccept = output()

  hideModal() {
    this.service.hide();
  }
}
