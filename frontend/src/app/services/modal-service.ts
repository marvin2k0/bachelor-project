import {Injectable, signal} from '@angular/core';

export interface ModalSettings {
  title: string,
  text: string,
  design?: "red" | "purple"
}

@Injectable({
  providedIn: 'root',
})
export class ModalService {
  private readonly _title = signal("")
  private readonly _text = signal("")
  private readonly _visible = signal<boolean>(false)
  private readonly _design = signal<"red" | "purple">("purple")

  title = this._title.asReadonly()
  text = this._text.asReadonly()
  visible = this._visible.asReadonly()
  design = this._design.asReadonly()

  show(settings: ModalSettings) {
    this._title.set(settings.title)
    this._text.set(settings.text)
    this._design.set(settings.design ?? "purple")
  }

  hide() {
    this._title.set("")
    this._text.set("")
    this._visible.set(false)
    this._design.set("purple")
  }
}
