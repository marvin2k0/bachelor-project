import {Injectable, signal} from '@angular/core';

export interface ModalSettings {
  title: string,
  text: string
}

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private readonly _title = signal("Header")
  private readonly _text = signal("Something")
  private readonly _visible = signal<boolean>(true)

  title = this._title.asReadonly()
  text = this._text.asReadonly()
  visible = this._visible.asReadonly()

  show(settings: ModalSettings) {
    this._title.set(settings.title)
    this._text.set(settings.text)
  }

  hide() {
    this._text.set("")
    this._visible.set(false)
  }
}
