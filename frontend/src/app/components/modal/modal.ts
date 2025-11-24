import {Component, inject, input, output} from '@angular/core';
import {ButtonComponent} from '../button/button.component';
import {ModalService} from '../../services/modal-service';

@Component({
  selector: 'app-modal',
  imports: [
    ButtonComponent
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
